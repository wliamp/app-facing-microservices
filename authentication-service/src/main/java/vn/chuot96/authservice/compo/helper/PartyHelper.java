package vn.chuot96.authservice.compo.helper;

import java.util.Map;
import reactor.core.publisher.Mono;
import vn.chuot96.authservice.dto.Tokens;

public interface PartyHelper {
    Mono<String> getSubject(String token);

    Mono<Tokens> issueToken(String token, Map<String, Object> claims);
}
