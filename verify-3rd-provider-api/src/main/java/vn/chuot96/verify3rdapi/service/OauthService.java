package vn.chuot96.verify3rdapi.service;

import static vn.chuot96.verify3rdapi.constant.Message.NOT_FOUND_OAUTH;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.verify3rdapi.component.OauthProvider;
import vn.chuot96.verify3rdapi.dto.RequestDTO;
import vn.chuot96.verify3rdapi.dto.UserDTO;
import vn.chuot96.verify3rdapi.exception.NoSupportedProviderException;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final ForwardService forwardService;

    private final List<OauthProvider> providers;

    public UserDTO verifyToken(String provider, RequestDTO request) {
        return providers.stream()
                .filter(p -> p.getProviderName().equalsIgnoreCase(provider))
                .findFirst()
                .map(p -> p.verifyToken(request.token()))
                .orElseThrow(() -> new NoSupportedProviderException(NOT_FOUND_OAUTH.getMsg()));
    }

    public Mono<ResponseEntity<?>> forward(String provider, RequestDTO request) {
        return forwardService
                .forwardAuthServiceLogin(request, verifyToken(provider, request))
                .then(forwardService.forwardJwtIssApiAccessRefresh())
                .map(ResponseEntity::ok);
    }
}
