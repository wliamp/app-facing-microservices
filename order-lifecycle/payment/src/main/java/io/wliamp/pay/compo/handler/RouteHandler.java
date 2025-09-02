package io.wliamp.pay.compo.handler;

import io.wliamp.pay.dto.SaleRequest;
import io.wliamp.pay.service.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class RouteHandler {
    private final SaleService saleService;

    public Mono<ServerResponse> sale(ServerRequest serverRequest) {
        String method = serverRequest.pathVariable("method");
        String currency = serverRequest.pathVariable("currency");
        String provider = serverRequest.pathVariable("provider");
        return serverRequest.bodyToMono(SaleRequest.class)
                .flatMap(s -> saleService.createSale(method, currency, provider, s))
                .flatMap(success -> ServerResponse.ok()
                        .bodyValue(Map.of(
                                "success", success,
                                "method", method,
                                "currency", currency,
                                "provider", provider
                        )))
                .onErrorResume(e -> {
                    log.error("[TRACE {}] Error in 'createSale[{}, {}, {}]' cause {}",
                            MDC.get("traceId"), method, currency, provider, e.getMessage(), e);
                    return ServerResponse.status(500)
                            .bodyValue(Map.of("error", e.getMessage()));
                });
    }

    public Mono<ServerResponse> authorize(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> capture(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> refund(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> cancel(ServerRequest request) {
        return null;
    }
}
