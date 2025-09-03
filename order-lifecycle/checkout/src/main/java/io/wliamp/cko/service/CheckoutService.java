package io.wliamp.cko.service;

import io.wliamp.cko.dto.Request;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class CheckoutService {
    private final ForwardService forwardService;

    private final OrderService orderService;

    public Mono<String> getPaymentUrl(String token, String method,
                                      String currency, String provider, Request request) {
        return orderService.addNew(token, request)
                .doOnSuccess(o -> log.info("'OrderService.addNew()' SUCCESS: Order={}", o))
                .doOnError(e -> log.error("[TRACE {}] ERROR in 'OrderService.addNew()' CAUSE {}",
                        MDC.get("traceId"), e.getMessage(), e)
                )
                .then(forwardService.fwPaymentSale(token, method, currency, provider, request))
                .doOnSuccess(purl -> log.info("'ForwardService.fwPaymentSale()' SUCCESS: purl={}", purl))
                .doOnError(e -> log.error("[TRACE {}] ERROR in 'ForwardService.fwPaymentSale()' CAUSE {}",
                        MDC.get("traceId"), e.getMessage(), e)
                );
    }
}
