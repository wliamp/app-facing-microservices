package vn.chuot96.chatservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import vn.chuot96.chatservice.dto.ChatMsg;

@Configuration
public class RedisConfig {
    @Bean
    public ReactiveRedisTemplate<String, ChatMsg> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory connectionFactory, ObjectMapper mapper) {
        Jackson2JsonRedisSerializer<ChatMsg> serializer = new Jackson2JsonRedisSerializer<>(mapper, ChatMsg.class);
        RedisSerializationContext<String, ChatMsg> serializationContext =
                RedisSerializationContext.<String, ChatMsg>newSerializationContext(new StringRedisSerializer())
                        .value(serializer)
                        .hashKey(new StringRedisSerializer())
                        .hashValue(serializer)
                        .build();
        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }
}
