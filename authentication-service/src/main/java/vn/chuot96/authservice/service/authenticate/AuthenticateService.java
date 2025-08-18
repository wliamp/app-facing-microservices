package vn.chuot96.authservice.service.authenticate;

import io.wliamp.token.util.InternalToken;

import java.time.Duration;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.compo.CacheHelper;
import vn.chuot96.authservice.compo.PartyHelper;
import vn.chuot96.authservice.compo.TokenHelper;
import vn.chuot96.authservice.dto.UserToken;
import vn.chuot96.authservice.service.data.*;
import vn.chuot96.authservice.util.Generator;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticateService {
    private final CommonService commonService;

    private final CacheService cacheService;

    private final AccService accService;

    private final CacheHelper cacheHelper;

    private final InternalToken internalToken;

    private final TokenHelper tokenHelper;

    private final Map<String, PartyHelper> loginHelpers;

    @Value("${cache.ttl.days}")
    private Duration CACHE_TTL;

    public Mono<ResponseEntity<UserToken>> guestLogin() {
        String cred = "guest:" + Generator.generateCode(32);
        return commonService.initAccountIfNotExists(cred)
                .doOnNext(id -> log.info("AccountId = {}", id))
                .flatMap(commonService::buildScopeAndAudiencesClaims)
                .doOnNext(c -> log.info("Claims built for {}", cred))
                .flatMap(claims -> tokenHelper.issueGuestToken(cred, claims))
                .doOnNext(tk -> log.info("Guest token issued for {}", cred))
                .flatMap(userToken ->
                        cacheHelper.put("auth:" + cred, userToken, CACHE_TTL)
                                .doOnSuccess(v -> log.info("Stored token in Redis for {}", cred))
                                .thenReturn(ResponseEntity.ok(userToken))
                );
    }

    public Mono<ResponseEntity<UserToken>> loginWithoutBearer(String party, String token) {
        PartyHelper partyHelper = loginHelpers.get(party.toLowerCase());
        if (partyHelper == null) {
            return Mono.error(new IllegalArgumentException("Unsupported login party: " + party));
        }
        return partyHelper
                .getSubject(token)
                .doOnSubscribe(s -> log.info("Subscribed to getSubject"))
                .doOnNext(sub -> log.info("Got subject (raw) = {}", sub))
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("getSubject returned empty");
                    return Mono.error(new RuntimeException("Invalid token"));
                }))
                .map(sub -> {
                    String subject = sub != null ? sub : "";
                    String cred = party.toLowerCase() + ":" + subject;
                    log.info("Built CRED = {}", cred);
                    return cred;
                })
                .flatMap(cred -> commonService.loginFlow(cred, partyHelper, token))
                .doOnNext(resp -> log.info("LOGIN FLOW RESPONSE = {}", resp))
                .doOnError(err -> log.error("LOGIN ERROR", err));
    }

    public Mono<ResponseEntity<UserToken>> linkAccount(String bearer, String party, String token) {
        String refreshToken = bearer.replace("Bearer ", "").trim();
        return Mono.zip(internalToken.verify(refreshToken), cacheHelper.isTokenBlacklisted(refreshToken))
                .flatMap(tuple -> {
                    if (!tuple.getT1() || tuple.getT2()) {
                        return Mono.error(new IllegalArgumentException("Invalid or blacklisted token"));
                    }
                    return internalToken.getClaims(refreshToken).flatMap(claims -> {
                        String oldCred = (String) claims.get("sub");
                        if (oldCred == null) {
                            return Mono.error(new IllegalArgumentException("Missing subject in old token"));
                        }
                        PartyHelper partyHelper = loginHelpers.get(party.toLowerCase());
                        if (partyHelper == null) {
                            return Mono.error(new IllegalArgumentException("Unsupported login party: " + party));
                        }
                        String newCred = party.toLowerCase() + ":" + partyHelper.getSubject(token);
                        return accService
                                .getAccountByCred(newCred)
                                .flatMap(existing -> Mono.<ResponseEntity<UserToken>>error(
                                        new IllegalStateException("This party account is already linked")))
                                .switchIfEmpty(accService
                                        .updateCred(oldCred, newCred)
                                        .then(cacheHelper.evict("auth:" + oldCred))
                                        .then(commonService.initAccountIfNotExists(newCred))
                                        .flatMap(commonService::buildScopeAndAudiencesClaims)
                                        .flatMap(claimsNew -> commonService.loginFlow(newCred, partyHelper, token)));
                    });
                });
    }

    public Mono<ResponseEntity<UserToken>> loginWithBearer(String bearer) {
        String refreshToken = bearer.replace("Bearer ", "").trim();
        return internalToken.verify(refreshToken).flatMap(valid -> {
            if (!valid) {
                return Mono.error(new IllegalArgumentException("Invalid refresh token"));
            }
            return internalToken.getClaims(refreshToken).flatMap(claims -> {
                String subject = (String) claims.get("sub");
                if (subject == null) {
                    return Mono.error(new IllegalArgumentException("Missing subject in refresh token"));
                }
                return cacheService.loadUserToken("auth:" + subject)
                        .switchIfEmpty(Mono.error(new IllegalStateException("Session expired, please login again")))
                        .flatMap(oldToken -> {
                            Mono<String> newAccess = internalToken.refresh(oldToken.access(), 3600);
                            Mono<String> newRefresh = internalToken.refresh(refreshToken, 2592000);
                            return Mono.zip(newAccess, newRefresh).flatMap(tuple -> {
                                UserToken newToken = new UserToken(tuple.getT1(), tuple.getT2());
                                return commonService.evictAndBlacklist(subject, oldToken, refreshToken)
                                        .then(cacheHelper.put("auth:" + subject, newToken, CACHE_TTL))
                                        .thenReturn(ResponseEntity.ok(newToken));
                            });
                        });
            });
        });
    }

    public Mono<Void> logout(String bearerToken) {
        String token = bearerToken.replace("Bearer ", "").trim();
        return internalToken.getClaims(token)
                .flatMap(claims -> {
                    String subject = claims.get("sub").toString();
                    UserToken dummyToken = new UserToken(token, token);
                    return commonService.evictAndBlacklist(subject, dummyToken, token);
                });
    }
}
