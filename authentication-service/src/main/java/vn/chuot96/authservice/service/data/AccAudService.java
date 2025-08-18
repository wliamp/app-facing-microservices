package vn.chuot96.authservice.service.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import vn.chuot96.authservice.model.AccAud;
import vn.chuot96.authservice.repo.AccAudRepo;
import vn.chuot96.authservice.repo.AudRepo;

@Service
@RequiredArgsConstructor
public class AccAudService {
    private final AccAudRepo accAudRepo;

    private final AudRepo audRepo;

    public Flux<AccAud> addNewAccount(Long accId) {
        return audRepo.findByStatusTrue()
                .map(aud -> AccAud.builder().accId(accId).audId(aud.getId()).build())
                .collectList()
                .flatMapMany(auds -> Flux.fromIterable(auds).flatMap(accAudRepo::save));
    }
}
