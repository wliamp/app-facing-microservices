package vn.chuot96.authservice.service.data;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.compo.handler.CacheHandler;
import vn.chuot96.authservice.dto.Tokens;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final CacheHandler cacheHandler;

    public Mono<Tokens> loadUserToken(String key) {
        return cacheHandler.get(key, Object.class).flatMap(obj -> switch (obj) {
            case Tokens token -> Mono.just(token);
            case Map<?, ?> map -> Mono.just(new Tokens((String) map.get("access"), (String) map.get("refresh")));
            case null, default -> Mono.empty();
        });
    }
}
