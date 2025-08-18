package vn.chuot96.authservice.compo;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.dto.UserToken;

@Component("fb")
@RequiredArgsConstructor
public class FacebookHelper implements PartyHelper {
    private final TokenHelper helper;

    @Override
    public String getParty() {
        return "facebook";
    }

    public String getSubject(String token) {
        return helper.getFacebookId(token).toString();
    }

    @Override
    public Mono<UserToken> issueToken(String token, Map<String, Object> claims) {
        return helper.issueTokenByFacebook(token, claims);
    }
}
