package io.wliamp.pay.compo.handler;

import io.wliamp.pay.compo.helper.VnPaySale;
import io.wliamp.pay.dto.SaleRequest;
import io.wliamp.pay.dto.Response;
import io.wliamp.pay.entity.Payment;
import io.wliamp.pay.repo.PaymentRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConsumeHandler {
    private final VnPaySale providerHelper;
    private final KafkaTemplate<String, Response> kafkaTemplate;
    private final PaymentRepo paymentRepo;

    @KafkaListener(topics = "order.request", groupId = "payment")
    public void consume(SaleRequest listen) {
        log.info("Received payment request for order {}", listen.orderId());
        Payment payment = Payment.builder()
                .orderId(listen.orderId())
                .amount(listen.amount())
                .method(listen.method())
                .currency(listen.currency())
                .description(listen.description())
                .ipAddress(listen.ipAddress())
                .status("PENDING")
                .build();
        Payment savedPayment = paymentRepo.save(payment).block();
        assert savedPayment != null;
        providerHelper.vnPaySale(, savedPayment)
                .subscribe(success -> {
                            savedPayment.setStatus(success ? "SUCCESS" : "FAILED");
                            paymentRepo.save(savedPayment);
                            kafkaTemplate.send("payment.response",
                                    listen.orderId(),
                                    Response.builder()
                                            .orderId(listen.orderId())
                                            .provider("vnPay")
                                            .status(savedPayment.getStatus())
                                            .build());
                        },
                        error -> {
                            savedPayment.setStatus("FAILED");
                            paymentRepo.save(savedPayment);
                            kafkaTemplate.send("payment.response",
                                    listen.orderId(),
                                    Response.builder()
                                            .orderId(listen.orderId())
                                            .status("FAILED")
                                            .errorMessage(error.getMessage())
                                            .build());
                        });

    }
}
