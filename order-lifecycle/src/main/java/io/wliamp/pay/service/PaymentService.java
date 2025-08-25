package io.wliamp.pay.service;

import io.wliamp.pay.compo.PaymentProcessor;
import io.wliamp.pay.constant.TransactionStatus;
import io.wliamp.pay.dto.Request;
import io.wliamp.pay.dto.Response;
import io.wliamp.pay.entity.Payment;
import io.wliamp.pay.repo.PaymentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepo repo;

    private final PaymentProcessor processor;

    public Mono<Response> processPayment(Request request) {
        return repo.save(Payment.builder()
                        .userId(request.userId())
                        .amount(request.amount())
                        .currency(request.currency())
                        .status(TransactionStatus.PENDING)
                        .createdAt(LocalDateTime.now())
                        .build())
                .flatMap(saved -> processor.execute(saved)
                        .flatMap(success -> {
                            saved.setStatus(success ? TransactionStatus.SUCCESS : TransactionStatus.FAILED);
                            return repo.save(saved);
                        }))
                .map(updated -> new Response(
                        updated.getId().toString(),
                        updated.getStatus(),
                        updated.getStatus() == TransactionStatus.SUCCESS ? "Payment successful" : "Payment failed"
                ));
    }
}

