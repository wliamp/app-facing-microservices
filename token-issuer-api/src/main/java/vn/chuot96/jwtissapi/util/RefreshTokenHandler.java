package vn.chuot96.jwtissapi.util;

import org.springframework.security.oauth2.jwt.JwtEncoder;
import vn.chuot96.jwtissapi.dto.TokenRequestDTO;

import java.time.Instant;
import java.util.UUID;

import static vn.chuot96.jwtissapi.constant.Token.REFRESH_TOKEN;

public class RefreshTokenHandler extends TokenHandler{
    @Override
    public String generate(JwtEncoder jwtEncoder, TokenRequestDTO request) {
        Instant now = Instant.now();
        long exp = now.getEpochSecond() + (request.duration() > 0 ? request.duration() : REFRESH_TOKEN.getDuration());
        String jti = UUID.randomUUID().toString();

        String formatted = String.format(
                REFRESH_TOKEN.getPattern(),
                request.subject(),
                iss,
                now.getEpochSecond(),
                exp,
                jti
        );

        return encode(jwtEncoder, formatted);
    }
}
