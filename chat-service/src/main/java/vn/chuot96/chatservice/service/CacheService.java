package vn.chuot96.chatservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.chuot96.chatservice.dto.ChatMsg;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final ReactiveRedisTemplate<String, ChatMsg> redisTemplate;

    public Mono<Long> cache(String channel, ChatMsg msg, int historyLimit) {
        ReactiveListOperations<String, ChatMsg> ops = redisTemplate.opsForList();
        String key = "chat:" + channel;
        return ops.leftPush(key, msg).flatMap(count -> redisTemplate
                .opsForList()
                .trim(key, 0, historyLimit - 1)
                .thenReturn(count));
    }

    public Flux<ChatMsg> load(String channel) {
        String key = "chat:" + channel;
        return redisTemplate.opsForList().range(key, 0, -1);
    }
}
