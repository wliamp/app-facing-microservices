package vn.chuot96.authservice.service.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import vn.chuot96.authservice.entity.Aud;
import vn.chuot96.authservice.repo.AudRepo;

@Service
@RequiredArgsConstructor
public class AudService {
    private final AudRepo audRepo;

    public Flux<Aud> getAudiencesByAccountId(Long accId) {
        return audRepo.findByAccId(accId);
    }
}
