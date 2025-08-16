package vn.chuot96.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import vn.chuot96.authservice.model.Scope;
import vn.chuot96.authservice.repo.ScopeRepo;

@Service
@RequiredArgsConstructor
public class ScopeService {
    private final ScopeRepo scopeRepo;

    public Flux<Scope> getScopesByAccountId(Long accId){
        return scopeRepo.findByAccId(accId);
    }
}
