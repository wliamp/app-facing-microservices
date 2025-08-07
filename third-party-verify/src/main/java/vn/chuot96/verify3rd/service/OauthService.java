package vn.chuot96.verify3rd.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.verify3rd.compo.OauthParty;
import vn.chuot96.verify3rd.dto.RequestDTO;
import vn.chuot96.verify3rd.dto.UserDTO;
import vn.chuot96.verify3rd.exception.NoSupportedProviderException;

import java.util.List;

import static vn.chuot96.verify3rd.constant.Message.NOT_FOUND_OAUTH;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final ForwardService forwardService;

    private final List<OauthParty> providers;

    public UserDTO verifyToken(String provider, RequestDTO request) {
        return providers.stream()
                .filter(p -> p.getProviderName().equalsIgnoreCase(provider))
                .findFirst()
                .map(p -> p.verifyToken(request.token()))
                .orElseThrow(() -> new NoSupportedProviderException(NOT_FOUND_OAUTH.getMsg()));
    }

    public Mono<ResponseEntity<?>> forward(String provider, RequestDTO request) {
        return forwardService
                .forwardAuthServiceLogin(verifyToken(provider, request), request.appCode())
                .then(forwardService.forwardJwtIssApiAccessRefresh(verifyToken(provider, request), request.appCode()))
                .map(ResponseEntity::ok);
    }
}
