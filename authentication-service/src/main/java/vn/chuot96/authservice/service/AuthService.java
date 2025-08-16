package vn.chuot96.authservice.service;

import io.wliamp.token.util.InternalToken;
import java.time.Duration;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.compo.CacheHelper;
import vn.chuot96.authservice.compo.PartyHelper;
import vn.chuot96.authservice.dto.UserToken;
import vn.chuot96.authservice.repo.AccRepo;
import vn.chuot96.authservice.repo.AudRepo;
import vn.chuot96.authservice.repo.ScopeRepo;
import vn.chuot96.authservice.util.Builder;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${cache.ttl.minutes}")
    private long cacheTtlMinutes;

    private final Duration CACHE_TTL_MINUTES = Duration.ofMinutes(cacheTtlMinutes);

    private final AccService accService;

    private final ScopeService scopeService;

    private final AudService audService;

    private final AccAudService accAudService;

    private final AccScopeService accScopeService;

    private final CacheHelper cache;

    private final InternalToken internal;

    private final Map<String, PartyHelper> loginHelpers;

    public Mono<ResponseEntity<UserToken>> loginWithoutBearer(String party, String token) {
        PartyHelper partyHelper = loginHelpers.get(party.toLowerCase());
        if (partyHelper == null) {
            return Mono.error(new IllegalArgumentException("Unsupported login party: " + party));
        }
        String cred = party.toLowerCase() + ":" + partyHelper.getSubject(token);
        Mono<Map<String, Object>> claimsMono = accService.getAccountByCred(cred)
                .flatMap(acc -> buildScopeAndAudiencesClaims(acc.getId()))
                .switchIfEmpty(accService.addNewAccount(cred).flatMap(accId -> Mono.when(
                                accScopeService.addNewAccount(accId).then(),
                                accAudService.addNewAccount(accId).then())
                        .then(buildScopeAndAudiencesClaims(accId))));
        return claimsMono.flatMap(claims -> issueTokenFlow(cred, claims, partyHelper, token));
    }

    public Mono<ResponseEntity<UserToken>> loginWithBearer(String bearer) {
        String refreshToken = bearer.replace("Bearer ", "").trim();
        return internal.verify(refreshToken).flatMap(valid -> {
            if (!valid) {
                return Mono.error(new IllegalArgumentException("Invalid refresh token"));
            }
            return internal.getClaims(refreshToken).flatMap(claims -> {
                String subject = (String) claims.get("sub");
                if (subject == null) {
                    return Mono.error(new IllegalArgumentException("Missing subject in refresh token"));
                }
                return cache.get("auth:" + subject, UserToken.class)
                        .switchIfEmpty(Mono.error(new IllegalStateException("Session expired, please login again")))
                        .flatMap(oldToken -> {
                            Mono<String> newAccess = internal.refresh(oldToken.access());
                            Mono<String> newRefresh = internal.refresh(refreshToken);
                            return Mono.zip(newAccess, newRefresh).flatMap(tuple -> {
                                UserToken newToken = new UserToken(tuple.getT1(), tuple.getT2());
                                return cache.put("auth:" + subject, newToken, CACHE_TTL_MINUTES)
                                        .thenReturn(ResponseEntity.ok(newToken));
                            });
                        });
            });
        });
    }

    public Mono<ResponseEntity<UserToken>> link(String bearer, String party, String token) {
        String refreshToken = bearer.replace("Bearer ", "").trim();
        return Mono.zip(internal.verify(refreshToken), cache.isTokenBlacklisted(refreshToken))
                .flatMap(tuple -> {
                    if (!tuple.getT1() || tuple.getT2()) {
                        return Mono.error(new IllegalArgumentException("Invalid or blacklisted token"));
                    }
                    return internal.getClaims(refreshToken).flatMap(claims -> {
                        String oldCred = (String) claims.get("sub");
                        if (oldCred == null) {
                            return Mono.error(new IllegalArgumentException("Missing subject in old token"));
                        }
                        PartyHelper partyHelper = loginHelpers.get(party.toLowerCase());
                        if (partyHelper == null) {
                            return Mono.error(new IllegalArgumentException("Unsupported login party: " + party));
                        }
                        String newCred = party.toLowerCase() + ":" + partyHelper.getSubject(token);
                        return accService.getAccountByCred(newCred)
                                .flatMap(existing -> Mono.<ResponseEntity<UserToken>>error(
                                        new IllegalStateException("This party account is already linked")))
                                .switchIfEmpty(accService
                                        .updateCred(oldCred, newCred)
                                        .then(cache.evict("auth:" + oldCred))
                                        .then(accService.getAccountByCred(newCred))
                                        .flatMap(acc -> buildScopeAndAudiencesClaims(acc.getId()))
                                        .flatMap(claimsNew -> issueTokenFlow(newCred, claimsNew, partyHelper, token)));
                    });
                });
    }

    public Mono<Void> logout(String bearerToken) {
        String token = bearerToken.replace("Bearer ", "");
        return internal.getClaims(token)
                .flatMap(claims -> {
                    // get credential from claim "sub"
                    String cred = claims.get("sub").toString();

                    // calculate remaining time
                    Object expObj = claims.get("exp");
                    long expSeconds = expObj != null ? Long.parseLong(expObj.toString()) : 0;
                    long nowSeconds = System.currentTimeMillis() / 1000;
                    long remainingSeconds = Math.max(0, expSeconds - nowSeconds);

                    // evict + blacklist
                    return cache.evict("auth:user:" + cred)
                            .then(cache.blacklistToken(token, Duration.ofSeconds(remainingSeconds)));
                })
                .then();
    }

    private Mono<Map<String, Object>> buildScopeAndAudiencesClaims(Long accId) {
        return Mono.zip(
                        scopeService.getScopesByAccountId(accId).collectList(),
                        audService.getAudiencesByAccountId(accId).collectList())
                .map(t -> Builder.buildTokenExtraClaims(t.getT1(), t.getT2()));
    }

    private Mono<ResponseEntity<UserToken>> issueTokenFlow(
            String cred, Map<String, Object> claims, PartyHelper partyHelper, String token) {
        return partyHelper.issueToken(token, claims).flatMap(userToken -> cache.put(
                        "auth:" + cred, userToken, CACHE_TTL_MINUTES)
                .thenReturn(ResponseEntity.ok(userToken)));
    }
}
