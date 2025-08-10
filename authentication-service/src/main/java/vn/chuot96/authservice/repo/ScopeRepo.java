package vn.chuot96.authservice.repo;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.model.Scope;

public interface ScopeRepo extends ReactiveCrudRepository<Scope, Long> {
    Mono<Scope> findByCode(String code);

    @Query("SELECT * FROM scopes WHERE status = true")
    Flux<Scope> findByStatusTrue();

    @Query(
            """
        SELECT s.*
        FROM scopes s
        JOIN account_scope as ON s.id = as.scope_id
        JOIN accounts a ON as.account_id = a.id
        WHERE a.id = :accId
        """)
    Flux<Scope> findByAccId(@Param("accCred") Long accId);
}
