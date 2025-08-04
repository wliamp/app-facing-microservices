package vn.chuot96.jwtissapi.component;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import vn.chuot96.jwtissapi.dto.RequestDTO;

@Component
@RequiredArgsConstructor
public class TokenCustomize {
    private final JwtEncoder jwtEncoder;

    public String generateAccess(RequestDTO request) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("your-issuer")
                .subject(request.subject())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600)) // 1h
                .claim("scope", request.scope())
                .claim("provider", request.provider())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateRefresh(RequestDTO request) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("your-issuer")
                .subject(request.subject())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(2592000)) // 30 days
                .claim("type", "refresh")
                .claim("provider", request.provider())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
