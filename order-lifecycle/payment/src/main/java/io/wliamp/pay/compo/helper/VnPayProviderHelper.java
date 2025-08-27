package io.wliamp.pay.compo.helper;

import io.github.wliamp.pro.pay.util.PaymentProvider;
import io.wliamp.pay.entity.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@Component("vn-pay")
@Slf4j
@RequiredArgsConstructor
public class VnPayProviderHelper implements ProviderHelper {
    private final PaymentProvider paymentProvider;

    @Override
    public Mono<Boolean> execute(Payment payment) {
        return paymentProvider.getVnPay().sale(Collections.emptyMap(), Map.of(
                        "orderId", payment.getOrderId(),
                        "amount", payment.getAmount(),
                        "description",
                        payment.getDescription() != null ? payment.getDescription() : "Payment for " + payment.getOrderId(),
                        "ipAddress", payment.getIpAddress(),
                        "paymentMethod", payment.getMethod()
                ))
                .map(resp -> {
                    @SuppressWarnings("unchecked")
                    Map<String, String> respMap = (Map<String, String>) resp;
                    String paymentUrl = respMap.get("paymentUrl");
                    log.info("[TRACE {}] VNPay payment URL generated for {}: {}",
                            MDC.get("traceId"), payment.getId(), paymentUrl);
                    return Map.of(
                            "paymentUrl", paymentUrl,
                            "paymentMethod", payment.getMethod(),
                            "orderId", payment.getId(),
                            "provider", "vn-pay"
                    );
                })
                .doOnError(error -> log.error("[TRACE {}] VNPay helper error for {}: {}",
                        MDC.get("traceId"), payment.getId(), error.getMessage()))
                .onErrorResume(error -> Mono.empty())
                .map(urlMap -> urlMap.get("paymentUrl") != null);
    }
}
