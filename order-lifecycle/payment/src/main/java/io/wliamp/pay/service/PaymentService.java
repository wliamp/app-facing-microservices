package io.wliamp.pay.service;

import io.wliamp.pay.dto.SaleRequest;
import io.wliamp.pay.entity.Payment;
import io.wliamp.pay.repo.OrderRepo;
import io.wliamp.pay.repo.PaymentRepo;
import io.wliamp.pay.repo.TagRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepo paymentRepo;

    private final OrderRepo orderRepo;

    private final TagRepo tagRepo;

    public Mono<Payment> addNew(String method, String currency, SaleRequest request) {
        return Mono.zip(
                        orderRepo.findIdByCode(request.orderId()),
                        tagRepo.findIdByCode("PAYMENT_CREATED"),
                        tagRepo.findIdByName(method),
                        tagRepo.findIdByName(currency)
                )
                .flatMap(tuple -> {
                    Long orderId = tuple.getT1();
                    Long statusId = tuple.getT2();
                    Long methodId = tuple.getT3();
                    Long currencyId = tuple.getT4();
                    return paymentRepo.save(Payment.builder()
                            .orderId(orderId)
                            .amount(BigDecimal.valueOf(Long.parseLong(request.amount())))
                            .method(methodId)
                            .currency(currencyId)
                            .status(statusId)
                            .build());
                });
    }

    public Mono<Void> setStatus(String code) {
        return tagRepo.findIdByCode(code)
                .flatMap(id -> paymentRepo.save(
                        Payment.builder().status(id).build()
                ))
                .then();
    }
}
