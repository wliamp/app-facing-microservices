package vn.chuot96.verify3rd.service;

import static vn.chuot96.verify3rd.constant.Message.NOT_FOUND_OAUTH;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vn.chuot96.verify3rd.compo.OauthParty;
import vn.chuot96.verify3rd.dto.User;
import vn.chuot96.verify3rd.exception.NoSupportedProviderException;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final ForwardService forwardService;

    private final List<OauthParty> parties;

    public User verifyToken(String provider, String token) {
        return parties.stream()
                .filter(p -> p.getName().equalsIgnoreCase(provider))
                .findFirst()
                .map(p -> p.verify(token))
                .orElseThrow(() -> new NoSupportedProviderException(NOT_FOUND_OAUTH.getMsg()));
    }

    public Mono<ResponseEntity<?>> forward(String provider, String token) {
        return forwardService
                .forwardAuthServiceLogin(verifyToken(provider, token))
                .then(forwardService.forwardJwtIssApiAccessRefresh(verifyToken(provider, token)))
                .map(ResponseEntity::ok);
    }
}
