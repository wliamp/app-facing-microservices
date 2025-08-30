package io.wliamp.pay.repo;

import io.wliamp.pay.entity.Order;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface OrderRepo extends R2dbcRepository<Order, Long> {
}
