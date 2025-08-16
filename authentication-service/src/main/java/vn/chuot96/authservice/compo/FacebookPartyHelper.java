package vn.chuot96.authservice.compo;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.dto.UserToken;

@Component
@RequiredArgsConstructor
public class FacebookPartyHelper implements PartyHelper {
    private final TokenHelper helper;

    @Override
    public String getParty() {
        return "facebook";
    }

    public String getSubject(String token) {
        return helper.getFacebookId(token).toString();
    }

    @Override
    public Mono<UserToken> issueToken(String token, Map<String, Object> extraClaims) {
        return helper.issueTokenByFacebook(token, extraClaims);
    }
}
