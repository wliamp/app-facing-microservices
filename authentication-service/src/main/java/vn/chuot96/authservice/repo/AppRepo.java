package vn.chuot96.authservice.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.model.App;

public interface AppRepo extends ReactiveCrudRepository<App, Long> {
    Mono<Long> findIdByCode(String code);
}
