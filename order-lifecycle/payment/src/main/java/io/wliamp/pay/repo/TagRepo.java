package io.wliamp.pay.repo;

import io.wliamp.pay.entity.Tag;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface TagRepo extends R2dbcRepository<Tag, Long> {
    Mono<Long> findIdByCode(String code);
    Mono<Long> findIdByName(String name);
}
