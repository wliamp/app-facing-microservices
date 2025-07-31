package vn.chuot96.tokenIssuerAPI.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import vn.chuot96.tokenIssuerAPI.dto.TokenRequestDTO;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static vn.chuot96.tokenIssuerAPI.constant.Token.ACCESS_TOKEN;

public class AccessTokenHandler {

    @Value("${jwt.issuer}")
    private String iss;

    public String generate(JwtEncoder jwtEncoder, TokenRequestDTO request) {
        Instant now = Instant.now();
        long exp = now.getEpochSecond() + (request.duration() > 0 ? request.duration() : ACCESS_TOKEN.getDuration());
        String jti = UUID.randomUUID().toString();

        String formatted = String.format(
                ACCESS_TOKEN.getPattern(),
                request.subject(),
                iss,
                now.getEpochSecond(),
                exp,
                jti,
                request.scope() != null ? request.scope() : "",
                request.audience() != null ? request.audience() : ""
        );

        return encode(jwtEncoder, formatted);
    }

    private String encode(JwtEncoder jwtEncoder, String formatted) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> claimsMap;
        try {
            claimsMap = mapper.readValue(formatted, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JWT claims", e);
        }

        JwtClaimsSet.Builder builder = JwtClaimsSet.builder();
        claimsMap.forEach(builder::claim);

        JwsHeader header = JwsHeader.with(() -> "HS256").build();
        JwtClaimsSet claims = builder.build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

}
