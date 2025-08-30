package io.wliamp.pay.service;

import io.wliamp.pay.entity.Order;
import io.wliamp.pay.repo.OrderRepo;
import io.wliamp.pay.repo.TagRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {
    private OrderRepo orderRepo;

    private TagRepo tagRepo;

    public Mono<Void> setStatus(String code) {
        return tagRepo.findIdByCode(code)
                .flatMap(id -> orderRepo.save(
                        Order.builder().status(id).build()
                ))
                .then();
    }
}
