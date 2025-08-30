package io.wliamp.pay.service;

import io.wliamp.pay.compo.helper.sale.ISale;
import io.wliamp.pay.dto.SaleRequest;
import io.wliamp.pay.entity.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SaleService {
    private final Map<String, ISale> sales;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public Mono<Boolean> createSale(String method, String currency, String provider, SaleRequest saleRequest) {
        return Mono.justOrEmpty(sales.get(provider.toLowerCase()))
                .doOnNext(sale -> log.info("[TRACE {}] Found sale handler for provider {}",
                        MDC.get("traceId"), provider))
                .flatMap(sale -> Mono.fromCallable(() -> sale.execute(saleRequest))
                        .doOnNext(resp -> log.info("[TRACE {}] Executed sale for {} - {}",
                                MDC.get("traceId"), provider, saleRequest.orderId())))
                .flatMap(resp -> {
                    @SuppressWarnings("unchecked")
                    Map<String, String> respM = (Map<String, String>) resp;
                    String paymentUrl = respM.get("paymentUrl");
                    log.info("[TRACE {}] Payment URL generated for {} - {}: {}",
                            MDC.get("traceId"), provider, saleRequest.orderId(), paymentUrl);
                    boolean success = paymentUrl != null;
                    if (!success) return Mono.just(false);
                    Mono<Void> orderUpdate = orderService.setStatus("ORDER_PENDING");
                    Mono<Long> paymentCreate = paymentService.addNew(method, currency, saleRequest);
                    Mono<Void> orderEvent = sendEvent("order.request",
                            "order:" + saleRequest.orderId() + ":ORDER_PENDING");
                    Mono<Void> paymentEvent = paymentCreate.flatMap(id ->
                            sendEvent("order.request",
                                    "payment:" + id + ":PAYMENT_CREATED")
                    );
                    return Mono.when(orderUpdate, paymentCreate, orderEvent, paymentEvent)
                            .thenReturn(true);
                })
                .doOnError(error -> log.error("[TRACE {}] {} helper error for {}: {}",
                        MDC.get("traceId"), provider, saleRequest.orderId(), error.getMessage(), error))
                .defaultIfEmpty(false)
                .doOnSuccess(result -> log.info("[TRACE {}] Final result for {} - {}: {}",
                        MDC.get("traceId"), provider, saleRequest.orderId(), result))
                .onErrorResume(error -> Mono.just(false));
    }

    private Mono<Void> sendEvent(String topic, String data) {
        return Mono.fromFuture(() -> kafkaTemplate.send(topic, data))
                .doOnSuccess(result -> log.info("Sent to {} offset={}",
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().offset()))
                .doOnError(e -> log.error("Failed to send Kafka event", e))
                .then();
    }
}
