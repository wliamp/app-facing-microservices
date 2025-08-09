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

    public User verify(String party, String token) {
        return parties.stream()
                .filter(p -> p.getName().equalsIgnoreCase(party))
                .findFirst()
                .map(p -> p.verify(token))
                .orElseThrow(() -> new NoSupportedProviderException(NOT_FOUND_OAUTH.getMsg()));
    }

    public Mono<ResponseEntity<?>> forward(String party, String token) {
        return forwardService
                .forwardAuthServiceLogin(verify(party, token))
                .then(forwardService.forwardJwtIssApiAccessRefresh(verify(party, token)))
                .map(ResponseEntity::ok);
    }
}
