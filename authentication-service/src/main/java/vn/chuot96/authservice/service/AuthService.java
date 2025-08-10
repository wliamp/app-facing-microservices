package vn.chuot96.authservice.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.compo.RedisHelper;
import vn.chuot96.authservice.dto.AuthRequest;
import vn.chuot96.authservice.dto.LinkRequest;
import vn.chuot96.authservice.dto.UserInfo;
import vn.chuot96.authservice.repo.AccRepo;
import vn.chuot96.authservice.repo.AudRepo;
import vn.chuot96.authservice.repo.ScopeRepo;
import vn.chuot96.authservice.util.Generator;
import vn.chuot96.authservice.util.Parser;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Duration CACHE_TTL = Duration.ofMinutes(15);
    private static final int MIN_TTL_SECONDS = 30;

    private final ForwardService forwardService;
    private final AccService accService;
    private final AccAudService accAudService;
    private final AccScopeService accScopeService;
    private final AccRepo accRepo;
    private final ScopeRepo scopeRepo;
    private final AudRepo audRepo;
    private final RedisHelper cacheHelper;
    private final ReactiveJwtDecoder jwtDecoder;

    public Mono<ResponseEntity<?>> handleGuest() {
        return createAndCache("guest", Generator.generateCode(32))
                .flatMap(forwardService::forwardJwtIss)
                .map(ResponseEntity::ok);
    }

    public Mono<ResponseEntity<?>> handleLogin(AuthRequest request) {
        String cred = request.party() + request.subject();
        return cacheHelper
                .getOrRefresh("auth:user:" + cred, UserInfo.class, MIN_TTL_SECONDS, CACHE_TTL, Mono.empty())
                .switchIfEmpty(accRepo.findByCred(cred)
                        .flatMap(acc -> buildAndCache(acc.getId(), cred))
                        .switchIfEmpty(createAndCache(request.party(), request.subject())))
                .map(ResponseEntity::ok);
    }

    public Mono<ResponseEntity<?>> handleLink(LinkRequest request, String bearerToken) {
        String oldCred = request.oldCred();
        String newCred = request.newCred().party() + request.newCred().subject();
        String token = bearerToken.replace("Bearer ", "");
        return evictAndBlacklist(oldCred, token)
                .then(accRepo.findByCred(oldCred)
                        .flatMap(acc -> buildAndCache(acc.getId(), acc.getCred()))
                        .switchIfEmpty(accService
                                .updateCred(oldCred, newCred)
                                .flatMap(acc -> buildAndCache(acc.getId(), acc.getCred()))))
                .map(ResponseEntity::ok);
    }

    public Mono<Void> handleLogout(AuthRequest request, String bearerToken) {
        String cred = request.party() + request.subject();
        String token = bearerToken.replace("Bearer ", "");
        return evictAndBlacklist(cred, token);
    }

    private Mono<UserInfo> createAndCache(String party, String subject) {
        String cred = party + subject;
        return accService.addNewAccount(party, subject).flatMap(accId -> {
            Mono<Void> scopesInsertMono = accScopeService.addNewAccount(accId).then();
            Mono<Void> audsInsertMono = accAudService.addNewAccount(accId).then();
            return Mono.when(scopesInsertMono, audsInsertMono).then(buildAndCache(accId, cred));
        });
    }

    private Mono<UserInfo> buildAndCache(Long accId, String cred) {
        return Mono.zip(
                        scopeRepo.findByAccId(accId).collectList(),
                        audRepo.findByAccId(accId).collectList())
                .map(tuple -> new UserInfo(cred, Parser.parseScope(tuple.getT1()), Parser.parseAudience(tuple.getT2())))
                .flatMap(userInfo -> cacheHelper
                        .put("auth:user:" + cred, userInfo, CACHE_TTL)
                        .thenReturn(userInfo));
    }

    private Mono<Void> evictAndBlacklist(String cred, String token) {
        return jwtDecoder
                .decode(token)
                .map(jwt -> {
                    assert jwt.getExpiresAt() != null;
                    long expSeconds = jwt.getExpiresAt().toEpochMilli() / 1000;
                    long nowSeconds = System.currentTimeMillis() / 1000;
                    return Math.max(0, expSeconds - nowSeconds);
                })
                .flatMap(remainingSeconds -> cacheHelper
                        .evict("auth:user:" + cred)
                        .then(cacheHelper.blacklistToken(token, Duration.ofSeconds(remainingSeconds))))
                .then();
    }
}
