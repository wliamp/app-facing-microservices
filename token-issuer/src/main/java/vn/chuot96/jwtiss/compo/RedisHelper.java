package vn.chuot96.jwtiss.compo;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class RedisHelper {
    private final ReactiveRedisTemplate<String, Object> redisTemplate;

    public <T> Mono<T> getOrRefresh(String key, Class<T> type, int minTtlSeconds, Duration ttl, Mono<T> dbQuery) {
        ReactiveValueOperations<String, Object> ops = redisTemplate.opsForValue();
        return ops.get(key)
                .flatMap(obj -> redisTemplate
                        .getExpire(key)
                        .defaultIfEmpty(Duration.ZERO)
                        .flatMap(remaining -> {
                            if (remaining.getSeconds() > minTtlSeconds) {
                                return Mono.just(type.cast(obj));
                            }
                            return refreshCache(key, ttl, dbQuery, ops);
                        }))
                .switchIfEmpty(refreshCache(key, ttl, dbQuery, ops));
    }

    private <T> Mono<T> refreshCache(
            String key, Duration ttl, Mono<T> dbQuery, ReactiveValueOperations<String, Object> ops) {
        return dbQuery.flatMap(value -> ops.set(key, value, ttl).thenReturn(value))
                .onErrorResume(e -> Mono.empty());
    }

    public <T> Mono<Boolean> put(String key, T value, Duration ttl) {
        return redisTemplate.opsForValue().set(key, value, ttl).onErrorResume(e -> Mono.just(false));
    }

    public Mono<Boolean> evict(String key) {
        return redisTemplate.delete(key).map(count -> count > 0);
    }

    public Mono<Boolean> blacklistToken(String token, Duration ttl) {
        return redisTemplate.opsForValue().set("blacklist:" + token, true, ttl);
    }

    public Mono<Boolean> isTokenBlacklisted(String token) {
        return redisTemplate.hasKey("blacklist:" + token);
    }
}
