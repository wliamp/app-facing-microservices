package vn.chuot96.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.repo.AppRepo;

@Service
@RequiredArgsConstructor
public class AppService {
    private final AppRepo appRepo;

    public Mono<Long> findIdByCode(String code) {
        return appRepo.findIdByCode(code);
    }
}
