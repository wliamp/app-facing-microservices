package vn.chuot96.authservice.compo.helper;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.compo.handler.TokenHandler;
import vn.chuot96.authservice.dto.UserToken;

@Component("google")
@RequiredArgsConstructor
public class GoogleHelper implements PartyHelper {
    private final TokenHandler helper;

    @Override
    public Mono<String> getSubject(String token) {
        return helper.getGoogleId(token);
    }

    @Override
    public Mono<UserToken> issueToken(String token, Map<String, Object> claims) {
        return helper.issueTokenByGoogle(token, claims);
    }
}
