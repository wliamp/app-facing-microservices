package io.wliamp.pay.compo.handler;

import io.wliamp.pay.compo.helper.VnPayHelper;
import io.wliamp.pay.dto.ListenEvent;
import io.wliamp.pay.dto.SendEvent;
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
    private final VnPayHelper vnPayHelper;
    private final KafkaTemplate<String, SendEvent> kafkaTemplate;
    private final PaymentRepo paymentRepo;

    @KafkaListener(topics = "order.request", groupId = "payment")
    public void consume(ListenEvent listen) {
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
        vnPayHelper.execute(savedPayment)
                .subscribe(success -> {
                            savedPayment.setStatus(success ? "SUCCESS" : "FAILED");
                            paymentRepo.save(savedPayment);
                            kafkaTemplate.send("payment.response",
                                    listen.orderId(),
                                    SendEvent.builder()
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
                                    SendEvent.builder()
                                            .orderId(listen.orderId())
                                            .status("FAILED")
                                            .errorMessage(error.getMessage())
                                            .build());
                        });

    }
}
