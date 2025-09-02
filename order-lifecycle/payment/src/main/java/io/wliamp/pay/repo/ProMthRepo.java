package io.wliamp.pay.repo;

import io.wliamp.pay.entity.ProMth;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProMthRepo extends R2dbcRepository<ProMth, Void> {
    @Query("""
            SELECT pm.name
            FROM provider_method pm
            JOIN tags p ON p.id = pm.provider
            JOIN tags m ON m.id = pm.method
            WHERE p.code = :provider
            AND m.code = :method
            """)
    Mono<String> getRealMethod(@Param("provider") String provider, @Param("method") String method);

    @Query("""
          SELECT t.code
          FROM tags t
          JOIN provider_method pm ON t.id = pm.provider
          WHERE pm.method =
          (SELECT id
          FROM tags
          WHERE code = :method)
          """)
    Flux<String> getProvidersByMethod(@Param("method") String method);
}
