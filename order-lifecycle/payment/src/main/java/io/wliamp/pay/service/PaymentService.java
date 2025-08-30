package io.wliamp.pay.service;

import io.wliamp.pay.entity.Payment;
import io.wliamp.pay.repo.PaymentRepo;
import io.wliamp.pay.repo.TagRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private PaymentRepo paymentRepo;

    private TagRepo tagRepo;

    public Mono<Void> setStatus(String code) {
        return tagRepo.findIdByCode(code)
                .flatMap(id -> paymentRepo.save(
                        Payment.builder().status(id).build()
                ))
                .then();
    }
}
