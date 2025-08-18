package vn.chuot96.authservice.service.authenticate;

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
import vn.chuot96.authservice.model.Acc;
import vn.chuot96.authservice.service.database.*;
import vn.chuot96.authservice.util.Builder;
import vn.chuot96.authservice.util.Generator;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final AccService accService;

    private final ScopeService scopeService;

    private final AudService audService;

    private final AccAudService accAudService;

    private final AccScopeService accScopeService;

    private final CacheHelper cache;

    private final InternalToken internal;

    private final Map<String, PartyHelper> loginHelpers;

    @Value("${cache.ttl.days}")
    private Duration CACHE_TTL;

    public Mono<ResponseEntity<UserToken>> guestLogin() {
        return loginFlow("guest:" + Generator.generateCode(8), loginHelpers.get("guest"), null);
    }

    public Mono<ResponseEntity<UserToken>> loginWithoutBearer(String party, String token) {
        PartyHelper partyHelper = loginHelpers.get(party.toLowerCase());
        if (partyHelper == null) {
            return Mono.error(new IllegalArgumentException("Unsupported login party: " + party));
        }
        String cred = party.toLowerCase() + ":" + partyHelper.getSubject(token);
        return loginFlow(cred, partyHelper, token);
    }

    public Mono<ResponseEntity<UserToken>> linkAccount(String bearer, String party, String token) {
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

                        return accService
                                .getAccountByCred(newCred)
                                .flatMap(existing -> Mono.<ResponseEntity<UserToken>>error(
                                        new IllegalStateException("This party account is already linked")))
                                .switchIfEmpty(accService
                                        .updateCred(oldCred, newCred)
                                        .then(cache.evict("auth:" + oldCred))
                                        .then(initAccountIfNotExists(newCred))
                                        .flatMap(this::buildScopeAndAudiencesClaims)
                                        .flatMap(claimsNew -> loginFlow(newCred, partyHelper, token)));
                    });
                });
    }

    private Mono<Long> initAccountIfNotExists(String cred) {
        return accService
                .getAccountByCred(cred)
                .map(Acc::getId)
                .switchIfEmpty(accService.addNewAccount(cred).flatMap(accId -> Mono.when(
                                accScopeService.addNewAccount(accId), accAudService.addNewAccount(accId))
                        .thenReturn(accId)));
    }

    private Mono<Map<String, Object>> buildScopeAndAudiencesClaims(Long accId) {
        return Mono.zip(
                        scopeService.getScopesByAccountId(accId).collectList(),
                        audService.getAudiencesByAccountId(accId).collectList())
                .map(t -> Builder.buildTokenExtraClaims(t.getT1(), t.getT2()));
    }

    private Mono<ResponseEntity<UserToken>> loginFlow(String cred, PartyHelper partyHelper, String token) {
        return initAccountIfNotExists(cred)
                .flatMap(this::buildScopeAndAudiencesClaims)
                .flatMap(claims -> partyHelper.issueToken(token, claims))
                .flatMap(userToken -> cache.put("auth:" + cred, userToken, CACHE_TTL)
                        .thenReturn(ResponseEntity.ok(userToken)));
    }
}
