package io.wliamp.cko.service;

import io.wliamp.cko.dto.Request;
import io.wliamp.cko.entity.Order;
import io.wliamp.cko.repo.OrderRepo;
import io.wliamp.cko.repo.TagRepo;
import io.wliamp.cko.util.Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;

    private final TagRepo tagRepo;

    public Mono<Void> addNew(Request request) {
        return tagRepo.findIdByCode("ORDER_STATUS_CREATED")
                .flatMap(id -> orderRepo.save(Order.builder()
                        .status(id)
                        .code(Generator.generateCode(8))
                        .userId(request.userId())
                        .amount(new BigDecimal(request.amount()))
                        .metadata(request.metadata().toString())
                        .build())
                )
                .then();
    }
}
