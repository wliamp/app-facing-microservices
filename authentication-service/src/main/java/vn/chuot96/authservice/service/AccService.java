package vn.chuot96.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.model.Acc;
import vn.chuot96.authservice.repo.AccRepo;
import vn.chuot96.authservice.util.Generator;

@Service
@RequiredArgsConstructor
public class AccService {
    private final AccRepo accRepo;

    public Mono<Acc> updateCred(String oldCred, String newCred) {
        return accRepo.findByCred(oldCred).flatMap(acc -> {
            acc.setCred(newCred);
            return accRepo.save(acc);
        });
    }

    public Mono<Long> addNewAccount(String key, String sub) {
        return accRepo.save(Acc.builder()
                        .code(Generator.generateCode(8))
                        .cred(key + sub)
                        .build())
                .map(Acc::getId);
    }
}
