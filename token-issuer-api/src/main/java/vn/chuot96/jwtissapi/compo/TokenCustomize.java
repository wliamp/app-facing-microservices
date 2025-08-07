package vn.chuot96.jwtissapi.compo;

import static vn.chuot96.jwtissapi.constant.Token.*;

import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import vn.chuot96.jwtissapi.dto.RequestDTO;

@Component
@RequiredArgsConstructor
public class TokenCustomize {
    private final JwtEncoder jwtEncoder;

    @Value("${jwt.issuer}")
    private final String iss;

    public String generateAccess(RequestDTO request) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(iss)
                .subject(request.subject())
                .audience(request.audience())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(ACCESS_TOKEN.getDuration()))
                .id(UUID.randomUUID().toString())
                .claim("type", ACCESS_TOKEN.getType())
                .claim("provider", request.provider())
                .claim("scope", request.scope())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateRefresh(RequestDTO request) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(iss)
                .subject(request.subject())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(REFRESH_TOKEN.getDuration()))
                .id(UUID.randomUUID().toString())
                .claim("type", REFRESH_TOKEN.getType())
                .claim("provider", request.provider())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
