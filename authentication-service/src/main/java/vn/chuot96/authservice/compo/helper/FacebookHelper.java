package vn.chuot96.authservice.compo.helper;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.compo.handler.TokenHandler;
import vn.chuot96.authservice.dto.Tokens;

@Component("facebook")
@RequiredArgsConstructor
public class FacebookHelper implements PartyHelper {
    private final TokenHandler helper;

    public Mono<String> getSubject(String token) {
        return helper.getFacebookId(token);
    }

    @Override
    public Mono<Tokens> issueToken(String token, Map<String, Object> claims) {
        return helper.issueTokenByFacebook(token, claims);
    }
}
