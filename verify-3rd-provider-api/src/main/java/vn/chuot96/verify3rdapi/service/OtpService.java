package vn.chuot96.verify3rdapi.service;

import static vn.chuot96.verify3rdapi.constant.Message.NOT_FOUND_OTP;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.verify3rdapi.component.OtpProvider;
import vn.chuot96.verify3rdapi.dto.RequestDTO;
import vn.chuot96.verify3rdapi.dto.UserDTO;
import vn.chuot96.verify3rdapi.exception.NoSupportedProviderException;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final ForwardService forwardService;

    private final List<OtpProvider> providers;

    public UserDTO verifyToken(String token) {
        return providers.stream()
                .filter(p -> p.supports(token))
                .findFirst()
                .map(p -> p.verifyToken(token))
                .orElseThrow(() -> new NoSupportedProviderException(NOT_FOUND_OTP.getMsg()));
    }

    public Mono<ResponseEntity<?>> forward(RequestDTO request) {
        return forwardService
                .forwardAuthServiceLogin(request, verifyToken(request.token()))
                .then(forwardService.forwardJwtIssApiAccessRefresh())
                .map(ResponseEntity::ok);
    }
}
