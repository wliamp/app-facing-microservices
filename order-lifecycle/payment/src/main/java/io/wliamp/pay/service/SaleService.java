package io.wliamp.pay.service;

import io.wliamp.pay.compo.helper.sale.ISale;
import io.wliamp.pay.dto.SaleRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SaleService {
    private final Map<String, ISale> sales;

    public Mono<Boolean> createSale(String provider, SaleRequest saleRequest) {
        return Mono.justOrEmpty(sales.get(provider.toLowerCase()))
                .doOnNext(sale -> log.info("[TRACE {}] Found sale handler for provider {}",
                        MDC.get("traceId"), provider))
                .flatMap(sale -> Mono.fromCallable(() -> sale.execute(saleRequest))
                        .doOnNext(resp -> log.info("[TRACE {}] Executed sale for {} - {}",
                                MDC.get("traceId"), provider, saleRequest.orderId())))
                .map(resp -> {
                    @SuppressWarnings("unchecked")
                    Map<String, String> respM = (Map<String, String>) resp;
                    String paymentUrl = respM.get("paymentUrl");
                    log.info("[TRACE {}] Payment URL generated for {} - {}: {}",
                            MDC.get("traceId"), provider, saleRequest.orderId(), paymentUrl);
                    return paymentUrl != null;
                })
                .doOnError(error -> log.error("[TRACE {}] {} helper error for {}: {}",
                        MDC.get("traceId"), provider, saleRequest.orderId(), error.getMessage(), error))
                .defaultIfEmpty(false)
                .doOnSuccess(result -> log.info("[TRACE {}] Final result for {} - {}: {}",
                        MDC.get("traceId"), provider, saleRequest.orderId(), result))
                .onErrorResume(error -> Mono.just(false));
    }
}
