package vn.chuot96.authservice.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.model.Acc;

public interface AccRepo extends ReactiveCrudRepository<Acc, Long> {
    Mono<Acc> findByCred(String credential);
}
