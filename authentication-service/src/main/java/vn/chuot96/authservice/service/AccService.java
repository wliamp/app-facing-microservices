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

    public Mono<Long> findIdByCredential(String credential) {
        return accRepo.findIdByCredential(credential);
    }

    public Mono<Long> insertGuestAcc(String credential) {
        return accRepo.save(Acc.builder()
                        .code(Generator.generateCode())
                        .credential(credential)
                        .build())
                .map(Acc::getId);
    }

    public Mono<Long> updateCredential(String current, String replace) {
        return accRepo.findByCredential(current)
                .flatMap(acc -> {
                    acc.setCredential(replace);
                    return accRepo.save(acc);
                })
                .map(Acc::getId);
    }
}
