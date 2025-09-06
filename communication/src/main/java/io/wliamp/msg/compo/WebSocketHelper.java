package io.wliamp.msg.compo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import io.wliamp.msg.dto.ChatMessage;
import io.wliamp.msg.service.CacheService;
import io.wliamp.msg.service.PersistenceService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.wliamp.msg.util.Extractor.extractChannel;
import static io.wliamp.msg.util.Safer.safeJson;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketHelper implements WebSocketHandler {

    private final ObjectMapper mapper;
    private final CacheService cacheService;
    private final PersistenceService persistenceService;

    @Value("${chat.history-limit}")
    private int historyLimit;

    @Value("${chat.max-message-length}")
    private int maxMessageLength;

    private final Map<String, Sinks.Many<ChatMessage>> channelSinks = new ConcurrentHashMap<>();

    private Sinks.Many<ChatMessage> getSink(String channel) {
        return channelSinks.computeIfAbsent(channel, k -> Sinks.many().multicast().directBestEffort());
    }

    @Override
    @NonNull
    public Mono<Void> handle(WebSocketSession session) {
        String channel = extractChannel(session.getHandshakeInfo().getUri()).orElse("global");
        log.debug("WebSocket session {} connected on channel '{}'", session.getId(), channel);

        return session
                .send(Flux.concat(
                                cacheService.load(channel)
                                        .map(msgSend -> safeJson(mapper, msgSend))
                                        .map(session::textMessage)
                                        .onErrorResume(e -> {
                                            log.error("Error reading history for channel {}", channel, e);
                                            return Flux.empty();
                                        }),
                                getSink(channel).asFlux()
                                        .map(msgSend -> safeJson(mapper, msgSend))
                                        .map(session::textMessage)
                                        .onErrorResume(e -> {
                                            log.error("Broadcast error on channel {}", channel, e);
                                            return Flux.empty();
                                        })
                        )
                )
                .and(session.receive()
                        .map(WebSocketMessage::getPayloadAsText)
                        .flatMap(payload -> parseMessage(payload, session.getId()))
                        .map(msg -> msg.channel().isBlank() ? msg.withChannel(channel) : msg)
                        .filter(msg -> msg.content() != null && !msg.content().isBlank())
                        .filter(msg -> {
                            if (msg.content().length() > maxMessageLength) {
                                log.warn("Message too long from {}: length={}", msg.sender(), msg.content().length());
                                return false;
                            }
                            return true;
                        })
                        .map(msg -> (msg.timestamp() == 0) ? msg.withTimestamp(System.currentTimeMillis()) : msg)
                        .flatMap(msg -> {
                            getSink(msg.channel()).tryEmitNext(msg);
                            return cacheService.cache(msg.channel(), msg, historyLimit)
                                    .then(persistenceService.persist(msg));
                        })
                        .doOnError(e -> log.error("Error in inbound stream", e))
                        .then()
                )
                .doOnSubscribe(s -> log.debug("WS session opened: {}", session.getId()))
                .doFinally(sig -> log.debug("WS session closed: {} ({})", session.getId(), sig));
    }

    private Mono<ChatMessage> parseMessage(String payload, String sessionId) {
        return Mono.fromCallable(() -> mapper.readValue(payload, ChatMessage.class))
                .onErrorResume(JsonProcessingException.class, e -> {
                    log.warn("Invalid payload from session {}: {}", sessionId, payload);
                    return Mono.empty();
                });
    }
}
