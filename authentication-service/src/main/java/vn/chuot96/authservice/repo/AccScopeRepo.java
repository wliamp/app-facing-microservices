package vn.chuot96.authservice.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import vn.chuot96.authservice.model.AccScope;

public interface AccScopeRepo extends ReactiveCrudRepository<AccScope, Void> {}
