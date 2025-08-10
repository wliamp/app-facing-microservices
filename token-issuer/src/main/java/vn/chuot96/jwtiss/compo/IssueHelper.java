package vn.chuot96.jwtiss.compo;

import static vn.chuot96.jwtiss.constant.Token.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import vn.chuot96.jwtiss.dto.Request;

@Component
@RequiredArgsConstructor
public class IssueHelper {
    private final JwtEncoder jwtEncoder;

    private final JwtDecoder jwtDecoder;

    @Value("${jwt.issuer}")
    private final String issuer;

    public String encodeAccess(Request request) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .subject(request.subject())
                .audience(request.audiences())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(ACCESS_EXP_SECONDS.getDuration()))
                .id(UUID.randomUUID().toString())
                .claim("type", "access")
                .claim("party", request.party())
                .claim("scope", request.scope())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String encodeRefresh(Request request) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .subject(request.subject())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(REFRESH_EXP_SECONDS.getDuration()))
                .id(UUID.randomUUID().toString())
                .claim("type", "refresh")
                .claim("party", request.party())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public boolean validateRefresh(String refresh) {
        try {
            Jwt jwt = jwtDecoder.decode(refresh);
            if (!"refresh".equals(jwt.getClaimAsString("type"))) {
                return false;
            }
            if (!issuer.equals(jwt.getIssuer().toString())) {
                return false;
            }
            assert jwt.getExpiresAt() != null;
            return !jwt.getExpiresAt().isBefore(Instant.now());
        } catch (JwtException e) {
            return false;
        }
    }

    public Duration getRefreshRemainingTime(String refresh) {
        try {
            Jwt jwt = jwtDecoder.decode(refresh);
            Instant expiresAt = jwt.getExpiresAt();
            Instant now = Instant.now();
            assert expiresAt != null;
            if (expiresAt.isBefore(now)) {
                return Duration.ZERO;
            }
            return Duration.between(now, expiresAt);
        } catch (JwtException e) {
            return Duration.ZERO;
        }
    }

    public Request parseRefresh(String refresh) {
        try {
            Jwt jwt = jwtDecoder.decode(refresh);
            String subject = jwt.getSubject();
            List<String> audiences = jwt.getAudience();
            String party = jwt.getClaimAsString("party");
            String scope = Optional.ofNullable(jwt.getClaimAsString("scope")).orElse("");
            return new Request(party, subject, scope, audiences);
        } catch (JwtException e) {
            throw new RuntimeException("Invalid refresh token");
        }
    }
}
