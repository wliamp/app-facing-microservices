package vn.chuot96.authservice.service;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.anno.ForwardPriority;
import vn.chuot96.authservice.dto.AuthRequest;
import vn.chuot96.authservice.dto.UserInfo;

@Service
@ForwardPriority
@RequiredArgsConstructor
public class AuthService {
    private final ForwardService forwardService;
    private final AccService accService;
    private final AppService appService;
    private final AccAppService accAppService;

    public Mono<ResponseEntity<?>> handleGuest(AuthRequest request) {
        String credential = request.provider() + request.subject();
        return accService
                .insertGuestAcc(credential)
                .flatMap(accId -> Mono.zip(Mono.just(accId), appService.findIdByCode(request.objectCode())))
                .flatMap(tuple ->
                        accAppService.insertAccApp(tuple.getT1(), tuple.getT2()).thenReturn(tuple.getT1()))
                .flatMap(accId -> forwardService.forwardJwtIssApi(
                        new UserInfo(request.provider(), request.subject(), "default", List.of("default"))))
                .map(ResponseEntity::ok);
    }

    public Mono<ResponseEntity<?>> handleRegister(AuthRequest request) {
        String credential = request.provider() + request.subject();
        Mono<Long> accMono = accService.findIdByCredential(credential);
        Mono<Long> appMono = appService.findIdByCode(request.objectCode());
        return accAppService
                .findIdByAccCredentialAndAppCode(credential, request.objectCode())
                .switchIfEmpty(Mono.zip(accMono, appMono)
                        .flatMap(tuple -> {
                            Long accId = tuple.getT1();
                            Long appId = tuple.getT2();
                            return accAppService.insertAccApp(accId, appId);
                        })
                        .then(Mono.just(-1L)))
                .thenReturn(ResponseEntity.ok(credential));
    }

    public Mono<ResponseEntity<?>> handleLink(AuthRequest request) {
        return accService
                .updateCredential(request.objectCode(), request.provider() + request.subject())
                .flatMap(accId -> forwardService.forwardJwtIssApi(new UserInfo(
                        request.provider(), request.subject(), "default", Collections.singletonList("default"))))
                .map(ResponseEntity::ok);
    }

    public Mono<Void> handleRemove(AuthRequest request) {
        return accAppService.deleteByAccCredentialAndAppCode(
                request.provider() + request.subject(), request.objectCode());
    }
}
