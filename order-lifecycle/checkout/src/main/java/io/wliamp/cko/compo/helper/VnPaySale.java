package io.wliamp.cko.compo.helper;

import io.github.wliamp.pro.pay.PaymentProvider;
import io.github.wliamp.pro.pay.VnPayRequest;
import io.wliamp.cko.dto.Request;
import io.wliamp.cko.service.OrderService;
import io.wliamp.cko.util.Extractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("vnPay")
@Slf4j
@RequiredArgsConstructor
public class VnPaySale implements ISale {
    private final PaymentProvider paymentProvider;

    private final OrderService orderService;

    @Override
    public Mono<Boolean> execute(String token, Request request) {
        String userId = Extractor.extractToken(token, 20);
        return orderService.create(token, request)
                .doOnSuccess(o -> log.info("Create Order for User[{}] SUCCESS: Order={}", userId, o))
                .doOnError(e -> log.error("[TRACE {}] Create Order for User[{}] FAILED cause {}",
                        MDC.get("traceId"), userId, e.getMessage(), e))
                .flatMap(o -> paymentProvider
                        .getVnPay()
                        .sale(VnPayRequest.builder()
                                .vnpAmount(request.amount())
                                .vnpIpAddr(request.ipAddress())
                                .vnpTxnRef(o.getCode())
                                .build())
                        .hasElement())
                .doOnSuccess(s -> log.info("Get Payment URL SUCCESS: purl={}", s))
                .doOnError(e -> log.error("[TRACE {}] Get Payment URL FAILED cause {}",
                        MDC.get("traceId"), e.getMessage(), e));
    }
}
