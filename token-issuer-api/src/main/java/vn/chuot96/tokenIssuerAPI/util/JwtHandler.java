package vn.chuot96.tokenIssuerAPI.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.util.Map;

public class JwtHandler {

    public static String encodeToken(JwtEncoder jwtEncoder, String formatted) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> claimsMap = null;
        try {
            claimsMap = mapper.readValue(formatted, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JwtClaimsSet.Builder builder = JwtClaimsSet.builder();
        for (Map.Entry<String, Object> entry : claimsMap.entrySet()) {
            builder.claim(entry.getKey(), entry.getValue());
        }

        JwsHeader header = JwsHeader.with(() -> "HS256").build();
        JwtClaimsSet claims = builder.build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
