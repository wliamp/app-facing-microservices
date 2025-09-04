package io.wliamp.cko.service;

import io.wliamp.cko.compo.helper.ISale;
import io.wliamp.cko.dto.Request;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CheckoutService {
    private final Map<String, ISale> sales;

    public Mono<String> getPaymentUrl(String token, String action,
                                      MultiValueMap<String, String> params, Request request) {
        String provider = params.getFirst("provider");
        return Mono.justOrEmpty(sales.get(provider))
                .doOnSuccess(p -> log.info("[TRACE {}] Found Provider on sales SUCCESS: provider={}",
                        MDC.get("traceId"), p))
                .doOnError(e -> log.error("[TRACE {}] Found Provider on sales FAILED CAUSE {}",
                        MDC.get("traceId"), e.getMessage(), e)
                )
                .flatMap(sale -> Mono.fromCallable(() -> sale.execute(token, request))
                .doOnSuccess(purl -> log.info("Execute sale SUCCESS: purl={}", purl)
                .doOnError(e -> log.error("[TRACE {}] Execute sale FAILED cause {}",
                        MDC.get("traceId"), e.getMessage(), e)
                )));
    }
}
