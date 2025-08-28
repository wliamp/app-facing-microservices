package io.wliamp.pay.compo.helper;

import io.wliamp.pay.entity.Payment;
import reactor.core.publisher.Mono;

public interface IHelper {
    Mono<Boolean> execute(Payment payment);
}
