package io.wliamp.pay.repo;

import io.wliamp.pay.entity.Payment;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PaymentRepo extends R2dbcRepository<Payment, Long> {
}
