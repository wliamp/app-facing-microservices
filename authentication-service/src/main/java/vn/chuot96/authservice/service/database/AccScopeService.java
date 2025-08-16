package vn.chuot96.authservice.service.database;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import vn.chuot96.authservice.model.AccScope;
import vn.chuot96.authservice.repo.AccScopeRepo;
import vn.chuot96.authservice.repo.ScopeRepo;

@Service
@RequiredArgsConstructor
public class AccScopeService {
    private final AccScopeRepo accScopeRepo;

    private final ScopeRepo scopeRepo;

    public Flux<AccScope> addNewAccount(Long accId) {
        return scopeRepo
                .findByStatusTrue()
                .map(scope ->
                        AccScope.builder().accId(accId).scopeId(scope.getId()).build())
                .collectList()
                .flatMapMany(auds -> Flux.fromIterable(auds).flatMap(accScopeRepo::save));
    }
}
