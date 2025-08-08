package vn.chuot96.authservice.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import vn.chuot96.authservice.model.AccApp;

public interface AccAppRepo extends ReactiveCrudRepository<AccApp, Void> {}
