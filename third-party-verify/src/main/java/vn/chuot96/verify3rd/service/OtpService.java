package vn.chuot96.verify3rd.service;

import static vn.chuot96.verify3rd.constant.Message.NOT_FOUND_OTP;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.verify3rd.compo.OtpParty;
import vn.chuot96.verify3rd.dto.User;
import vn.chuot96.verify3rd.exception.NoSupportedProviderException;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final ForwardService forwardService;

    private final List<OtpParty> parties;

    public User verifyToken(String token) {
        return parties.stream()
                .filter(p -> p.supports(token))
                .findFirst()
                .map(p -> p.verify(token))
                .orElseThrow(() -> new NoSupportedProviderException(NOT_FOUND_OTP.getMsg()));
    }

    public Mono<ResponseEntity<?>> forward(String token) {
        return forwardService
                .forwardAuthServiceLogin(verifyToken(token))
                .then(forwardService
                        .forwardJwtIssApiAccessRefresh(verifyToken(token))
                        .map(ResponseEntity::ok));
    }
}
