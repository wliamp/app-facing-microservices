package vn.chuot96.verify3rd.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.verify3rd.compo.OtpParty;
import vn.chuot96.verify3rd.dto.RequestDTO;
import vn.chuot96.verify3rd.dto.UserDTO;
import vn.chuot96.verify3rd.exception.NoSupportedProviderException;

import java.util.List;

import static vn.chuot96.verify3rd.constant.Message.NOT_FOUND_OTP;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final ForwardService forwardService;

    private final List<OtpParty> providers;

    public UserDTO verifyToken(String token) {
        return providers.stream()
                .filter(p -> p.supports(token))
                .findFirst()
                .map(p -> p.verifyToken(token))
                .orElseThrow(() -> new NoSupportedProviderException(NOT_FOUND_OTP.getMsg()));
    }

    public Mono<ResponseEntity<?>> forward(RequestDTO request) {
        return forwardService
                .forwardAuthServiceLogin(verifyToken(request.token()), request.appCode())
                .then(forwardService.forwardJwtIssApiAccessRefresh(verifyToken(request.token()), request.appCode()))
                .map(ResponseEntity::ok);
    }
}
