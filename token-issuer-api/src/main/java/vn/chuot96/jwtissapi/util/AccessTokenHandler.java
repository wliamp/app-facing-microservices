package vn.chuot96.jwtissapi.util;

import org.springframework.security.oauth2.jwt.JwtEncoder;
import vn.chuot96.jwtissapi.dto.RequestDTO;

import java.time.Instant;
import java.util.UUID;

import static vn.chuot96.jwtissapi.constant.Token.ACCESS_TOKEN;

public class AccessTokenHandler extends TokenHandler {

    public String generate(JwtEncoder jwtEncoder, RequestDTO request) {
        Instant now = Instant.now();
        long exp = now.getEpochSecond() + ACCESS_TOKEN.getDuration();
        String jti = UUID.randomUUID().toString();

        String formatted = String.format(
                ACCESS_TOKEN.getPattern(),
                request.provider() + request.subject(),
                issuer,
                now.getEpochSecond(),
                exp,
                jti,
                request.scope(),
                request.audience()
        );

        return encode(jwtEncoder, formatted);
    }

}
