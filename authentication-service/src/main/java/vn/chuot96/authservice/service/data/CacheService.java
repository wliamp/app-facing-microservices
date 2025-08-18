package vn.chuot96.authservice.service.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.compo.CacheHelper;
import vn.chuot96.authservice.dto.UserToken;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final CacheHelper cacheHelper;

    public Mono<UserToken> loadUserToken(String key) {
        return cacheHelper.get(key, Object.class)
                .flatMap(obj -> switch (obj) {
                    case UserToken token -> Mono.just(token);
                    case Map<?, ?> map -> Mono.just(new UserToken(
                            (String) map.get("access"),
                            (String) map.get("refresh")
                    ));
                    case null, default -> Mono.empty();
                });
    }
}
