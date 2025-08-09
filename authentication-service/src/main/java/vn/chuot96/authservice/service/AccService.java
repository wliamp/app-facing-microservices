package vn.chuot96.authservice.service;

import java.util.List;
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

    public Mono<Acc> insertGuestAcc() {
        return accRepo.save(Acc.builder()
                .code(Generator.generateCode(8))
                .credential("guest" + Generator.generateCode(32))
                .scope("profile:read profile:write token:get")
                .audiences(List.of("authentication-service", "profile-service", "token-issuer", "third-party-verify"))
                .build());
    }

    public Mono<Acc> updateCredential(String current, String replace) {
        return accRepo.findByCredential(current)
                .flatMap(acc -> {
                    acc.setCredential(replace);
                    return accRepo.save(acc);
                });
    }
}
