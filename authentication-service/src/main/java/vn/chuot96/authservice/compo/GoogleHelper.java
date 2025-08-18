package vn.chuot96.authservice.compo;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.dto.UserToken;

@Component("gg")
@RequiredArgsConstructor
public class GoogleHelper implements PartyHelper {
    private final TokenHelper helper;

    @Override
    public Mono<String> getSubject(String token) {
        return helper.getGoogleSub(token);
    }

    @Override
    public Mono<UserToken> issueToken(String token, Map<String, Object> claims) {
        return helper.issueTokenByGoogle(token, claims);
    }
}
