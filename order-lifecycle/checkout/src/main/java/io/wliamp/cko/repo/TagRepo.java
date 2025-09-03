package io.wliamp.cko.repo;

import io.wliamp.cko.entity.Tag;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface TagRepo extends R2dbcRepository<Tag, Long> {
    Mono<Long> findIdByCode(String code);
}
