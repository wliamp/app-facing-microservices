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
import vn.chuot96.authservice.dto.UserToken;
import vn.chuot96.authservice.model.Acc;
import vn.chuot96.authservice.service.data.*;
import vn.chuot96.authservice.util.Builder;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommonService {
    private final AccService accService;

    private final AudService audService;

    private final ScopeService scopeService;

    private final AccAudService accAudService;

    private final AccScopeService accScopeService;

    private final InternalToken internalToken;

    private final CacheHelper cacheHelper;

    private final CacheService cacheService;

    @Value("${cache.ttl.days}")
    private Duration CACHE_TTL;

    public Mono<Long> initAccountIfNotExists(String cred) {
        return accService
                .getAccountByCred(cred)
                .doOnNext(acc -> log.info("Found existing account for {}", cred))
                .map(Acc::getId)
                .switchIfEmpty(accService
                        .addNewAccount(cred)
                        .flatMap(accId -> Mono.when(
                                        accScopeService.addNewAccount(accId), accAudService.addNewAccount(accId))
                                .thenReturn(accId))
                        .doOnNext(newId -> log.info("Created new account {} for {}", newId, cred)));
    }

    public Mono<Map<String, Object>> buildScopeAndAudiencesClaims(Long accId) {
        return Mono.zip(
                        scopeService.getScopesByAccountId(accId).collectList(),
                        audService.getAudiencesByAccountId(accId).collectList())
                .map(t -> Builder.buildTokenExtraClaims(t.getT1(), t.getT2()));
    }

    public Mono<ResponseEntity<UserToken>> loginFlow(String cred, PartyHelper partyHelper, String token) {
        return initAccountIfNotExists(cred)
                .doOnNext(id -> log.info("AccountId = {}", id))
                .flatMap(this::buildScopeAndAudiencesClaims)
                .doOnNext(c -> log.info("Claims built"))
                .flatMap(claims -> partyHelper.issueToken(token, claims))
                .doOnNext(tk -> log.info("Token issued"))
                .flatMap(userToken ->
                        cacheHelper.put("auth:" + cred, userToken, CACHE_TTL).thenReturn(ResponseEntity.ok(userToken)));
    }

    public Mono<Void> evictAndBlacklist(String subject, UserToken oldToken, String refreshToken) {
        long now = System.currentTimeMillis() / 1000;

        long accessExp = getExp(oldToken.access());
        long accessTTL = Math.max(0, accessExp - now);

        long refreshExp = getExp(refreshToken);
        long refreshTTL = Math.max(0, refreshExp - now);

        return cacheHelper.evict("auth:" + subject)
                .then(cacheHelper.blacklistToken(oldToken.access(), Duration.ofSeconds(accessTTL)))
                .then(cacheHelper.blacklistToken(refreshToken, Duration.ofSeconds(refreshTTL))).then();
    }

    public long getExp(String token) {
        try {
            Map<String, Object> claims = internalToken.getClaims(token).block();
            assert claims != null;
            Object expObj = claims.get("exp");
            return expObj != null ? Long.parseLong(expObj.toString()) : 0;
        } catch (Exception e) {
            return 0;
        }
    }
}
