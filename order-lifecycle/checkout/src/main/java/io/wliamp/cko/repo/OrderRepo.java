package io.wliamp.cko.repo;

import io.wliamp.cko.entity.Order;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface OrderRepo extends R2dbcRepository<Order, Long> {
}
