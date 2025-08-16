package vn.chuot96.authservice.service.authenticate;

import io.wliamp.token.util.InternalToken;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.compo.CacheHelper;

@Service
@RequiredArgsConstructor
public class LogoutService {
    private final InternalToken internal;

    private final CacheHelper cache;

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
}
