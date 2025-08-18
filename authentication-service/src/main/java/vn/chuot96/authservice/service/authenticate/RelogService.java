package vn.chuot96.authservice.service.authenticate;

import io.wliamp.token.util.InternalToken;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.compo.CacheHelper;
import vn.chuot96.authservice.dto.UserToken;

@Service
@RequiredArgsConstructor
public class RelogService {
    private final InternalToken internal;

    private final CacheHelper cache;

    @Value("${cache.ttl.days}")
    private Duration CACHE_TTL;

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
                            Mono<String> newAccess = internal.refresh(oldToken.access(), 3600);
                            Mono<String> newRefresh = internal.refresh(refreshToken, 2592000);
                            return Mono.zip(newAccess, newRefresh).flatMap(tuple -> {
                                UserToken newToken = new UserToken(tuple.getT1(), tuple.getT2());
                                return cache.put("auth:" + subject, newToken, CACHE_TTL)
                                        .thenReturn(ResponseEntity.ok(newToken));
                            });
                        });
            });
        });
    }
}
