package io.wliamp.cko.compo.handler;

import io.wliamp.cko.dto.Request;
import io.wliamp.cko.service.CheckoutService;
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
    private final CheckoutService checkoutService;

    public Mono<ServerResponse> sale(ServerRequest serverRequest) {
        String token = serverRequest.headers().firstHeader("X-Access-Token");
        String method = serverRequest.pathVariable("method");
        String currency = serverRequest.pathVariable("currency");
        String provider = serverRequest.pathVariable("provider");
        return serverRequest.bodyToMono(Request.class)
                .flatMap(s -> checkoutService.getPaymentUrl(token, method, currency, provider, s))
                .flatMap(purl -> ServerResponse.ok()
                        .bodyValue(Map.of(
                                "purl", purl
                        )))
                .onErrorResume(e -> {
                    log.error("[TRACE {}] ERROR in 'CheckoutService.getPaymentUrl()' CAUSE {}",
                            MDC.get("traceId"), e.getMessage(), e);
                    return ServerResponse.status(500)
                            .bodyValue(Map.of("error", e.getMessage()));
                });
    }
}
