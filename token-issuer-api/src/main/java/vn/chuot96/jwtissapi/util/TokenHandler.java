package vn.chuot96.jwtissapi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import vn.chuot96.jwtissapi.dto.RequestDTO;

import java.util.Map;

public abstract class TokenHandler {

    @Value("${jwt.issuer}")
    protected static String issuer;

    protected static String encode(JwtEncoder jwtEncoder, String formattedJson) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> claimsMap;
        try {
            claimsMap = mapper.readValue(formattedJson, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JWT claims", e);
        }

        JwtClaimsSet.Builder builder = JwtClaimsSet.builder();
        claimsMap.forEach(builder::claim);

        return jwtEncoder
                .encode(JwtEncoderParameters
                .from(JwsHeader.with(() -> "HS256").build(),
                        builder.build())).getTokenValue();
    }

    public abstract String generate(JwtEncoder jwtEncoder, RequestDTO request);

}