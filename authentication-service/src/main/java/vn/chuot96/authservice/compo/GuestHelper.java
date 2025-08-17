package vn.chuot96.authservice.compo;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.dto.UserToken;
import vn.chuot96.authservice.util.Generator;

@Component("guest")
@RequiredArgsConstructor
public class GuestHelper implements PartyHelper {
    private final TokenHelper tokenHelper;

    @Override
    public String getParty() {
        return "guest";
    }

    @Override
    public String getSubject(String token) {
        return Generator.generateCode(8);
    }

    @Override
    public Mono<UserToken> issueToken(String token, Map<String, Object> claims) {
        String sub = getParty() + ":" + getSubject(token);
        return tokenHelper.issueGuestToken(sub, claims);
    }
}
