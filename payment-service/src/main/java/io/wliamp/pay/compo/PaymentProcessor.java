package io.wliamp.pay.compo;

import io.wliamp.pay.entity.Payment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class PaymentProcessor {
    private final WebClient webClient;

    public PaymentProcessor(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://mock-gateway.example.com").build();
    }

    public Mono<Boolean> execute(Payment payment) {
        return webClient.post()
                .uri("/pay")
                .bodyValue(payment)
                .retrieve()
                .bodyToMono(Map.class)
                .map(resp -> Boolean.TRUE.equals(resp.get("success")))
                .onErrorReturn(false); // fallback
    }
}
