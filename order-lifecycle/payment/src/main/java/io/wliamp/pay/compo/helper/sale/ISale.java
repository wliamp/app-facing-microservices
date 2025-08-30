package io.wliamp.pay.compo.helper.sale;

import io.wliamp.pay.dto.SaleRequest;
import reactor.core.publisher.Mono;

public interface ISale {
    Mono<Boolean> execute(SaleRequest saleRequest);
}
