package io.wliamp.pay.repo;

import io.wliamp.pay.entity.ProCur;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProCurRepo extends R2dbcRepository<ProCur, Void> {
    @Query("""
            SELECT pc.name
            FROM provider_currency pc
            JOIN tags p ON p.id = pc.provider
            JOIN tags c ON c.id = pm.currency
            WHERE p.code = :provider
            AND c.code = :currency
            """)
    Mono<String> getRealCurrency(@Param("provider") String provider, @Param("currency") String currency);

    @Query("""
          SELECT t.code
          FROM tags t
          JOIN provider_currency pc ON t.id = pc.provider
          WHERE pc.currency =
          (SELECT id
          FROM tags
          WHERE code = :currency)
          """)
    Flux<String> getProvidersByCurrency(@Param("currency") String currency);
}
