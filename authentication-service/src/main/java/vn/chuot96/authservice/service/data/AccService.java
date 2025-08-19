package vn.chuot96.authservice.service.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.entity.Acc;
import vn.chuot96.authservice.repo.AccRepo;
import vn.chuot96.authservice.util.Generator;

@Service
@RequiredArgsConstructor
public class AccService {
    private final AccRepo accRepo;

    public Mono<Acc> getAccountByCred(String cred) {
        return accRepo.findByCred(cred);
    }

    public Mono<Acc> updateCred(String oldCred, String newCred) {
        return accRepo.findByCred(oldCred).flatMap(acc -> {
            acc.setCred(newCred);
            return accRepo.save(acc);
        });
    }

    public Mono<Long> addNewAccount(String cred) {
        return accRepo.save(
                        Acc.builder().code(Generator.generateCode(8)).cred(cred).build())
                .map(Acc::getId);
    }
}
