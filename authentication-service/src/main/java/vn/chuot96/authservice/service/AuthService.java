package vn.chuot96.authservice.service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.compo.RedisHelper;
import vn.chuot96.authservice.dto.AuthRequest;
import vn.chuot96.authservice.dto.UserInfo;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Duration CACHE_TTL = Duration.ofMinutes(15);
    private static final int MIN_TTL_SECONDS = 30;

    @Value("${app.code}")
    private final String appCode;

    private final ForwardService forwardService;
    private final AccService accService;
    private final AppService appService;
    private final AccAppService accAppService;
    private final RedisHelper cacheHelper;
    private final ReactiveJwtDecoder jwtDecoder;

    public Mono<ResponseEntity<?>> handleGuest() {
        return accService
                .insertGuestAcc()
                .flatMap(acc -> Mono.zip(
                        Mono.just(acc),
                        cacheHelper.getOrRefresh(
                                "auth:accId:" + acc.getId(),
                                Long.class,
                                MIN_TTL_SECONDS,
                                CACHE_TTL,
                                appService.findIdByCode(appCode))))
                .flatMap(tuple -> accAppService
                        .insertAccApp(tuple.getT1().getId(), tuple.getT2())
                        .thenReturn(tuple.getT1()))
                .flatMap(acc -> cacheHelper
                        .put("auth:accId" + acc.getId(), acc, CACHE_TTL)
                        .thenReturn(acc))
                .flatMap(acc -> forwardService.forwardJwtIss(
                        new UserInfo(acc.getCredential(), acc.getScope(), acc.getAudiences())))
                .map(ResponseEntity::ok);
    }

    public Mono<ResponseEntity<?>> handleLogin(AuthRequest request) {
        String credential = request.provider() + request.subject();
        Mono<Long> accMono = cacheHelper.getOrRefresh(
                "auth:accId:" + credential,
                Long.class,
                MIN_TTL_SECONDS,
                CACHE_TTL,
                accService.findIdByCredential(credential));
        Mono<Long> appMono = cacheHelper.getOrRefresh(
                "auth:appId" + appCode,
                Long.class,
                MIN_TTL_SECONDS,
                CACHE_TTL,
                appService.findIdByCode(appCode));
        return accAppService
                .findIdByAccCredentialAndAppCode(credential, appCode)
                .switchIfEmpty(Mono.zip(accMono, appMono)
                        .flatMap(tuple -> accAppService.insertAccApp(tuple.getT1(), tuple.getT2()))
                        .then(Mono.just(-1L)))
                .flatMap(res -> accMono.flatMap(accId -> cacheHelper.put("auth:accId:" + credential, accId, CACHE_TTL))
                        .thenReturn(res))
                .thenReturn(ResponseEntity.ok(credential));
    }

    public Mono<ResponseEntity<?>> handleLink(AuthRequest request) {
        String newCredential = request.provider() + request.subject();
        return accService
                .updateCredential(request.objectCode(), newCredential)
                .flatMap(acc -> {
                    return cacheHelper
                            .put("auth:accId" + newCredential, acc, CACHE_TTL)
                            .thenReturn(acc);
                })
                .flatMap(acc -> forwardService.forwardJwtIss(new UserInfo(
                        acc.getCredential(), acc.getScope(), acc.getAudiences())))
                .map(ResponseEntity::ok);
    }

    public Mono<Void> handleLogout(AuthRequest request, String bearerToken) {
        String credential = request.provider() + request.subject();
        String token = bearerToken.replace("Bearer ", "");
        return jwtDecoder
                .decode(token)
                .map(jwt -> {
                    assert jwt.getExpiresAt() != null;
                    long expSeconds = jwt.getExpiresAt().toEpochMilli() / 1000;
                    long nowSeconds = System.currentTimeMillis() / 1000;
                    return Math.max(0, expSeconds - nowSeconds);
                })
                .flatMap(remainingSeconds -> cacheHelper
                        .evict("auth:" + credential)
                        .then(cacheHelper.blacklistToken(token, Duration.ofSeconds(remainingSeconds))))
                .then();
    }
}
