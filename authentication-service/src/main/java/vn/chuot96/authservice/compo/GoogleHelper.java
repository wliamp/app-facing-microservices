package vn.chuot96.authservice.compo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.dto.UserToken;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class GoogleHelper implements PartyHelper {
    private final TokenHelper helper;

    @Override
    public String getParty() {
        return "google";
    }

    @Override
    public String getSubject(String token) {
        return helper.getGoogleSub(token).toString();
    }

    @Override
    public Mono<UserToken> issueToken(String token, Map<String, Object> claims) {
        return helper.issueTokenByGoogle(token, claims);
    }
}
