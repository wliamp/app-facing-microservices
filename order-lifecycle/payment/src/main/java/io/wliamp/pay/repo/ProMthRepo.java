package io.wliamp.pay.repo;

import io.wliamp.pay.entity.ProMth;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

public interface ProMthRepo extends R2dbcRepository<ProMth, Void> {
    @Query("""
            SELECT pm.name
            FROM provider_method pm
            JOIN tag pro ON pro.id = pm.provider
            JOIN tag mth ON mth.id = pm.method
            WHERE pro.code = :name
            AND mth.code = :name
            """)
    Mono<String> getRealMethod(@Param("provider") String provider, @Param("method") String method);
}
