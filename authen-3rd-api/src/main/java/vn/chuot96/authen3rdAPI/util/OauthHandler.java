package vn.chuot96.authen3rdAPI.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.client.RestTemplate;
import vn.chuot96.authen3rdAPI.dto.ResponseDTO;
import vn.chuot96.authen3rdAPI.exception.InvalidTokenException;

import java.util.Map;

import static vn.chuot96.authen3rdAPI.constant.AuthMessage.INVALID_FACEBOOK_TOKEN;
import static vn.chuot96.authen3rdAPI.constant.AuthMessage.INVALID_GOOGLE_TOKEN;
import static vn.chuot96.authen3rdAPI.constant.AuthProvider.*;

public class OauthHandler {

    private static final RestTemplate restTemplate = new RestTemplate();

    public static ResponseDTO googleProvider(String token) {
        try {
            JwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(GOOGLE.getEndpoint()).build();
            Jwt jwt = decoder.decode(token);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> info = Map.of(
                    "email", jwt.getClaimAsString("email"),
                    "name", jwt.getClaimAsString("name"),
                    "picture", jwt.getClaimAsString("picture"),
                    "sub", jwt.getSubject()
            );

            String rawJson = mapper.writeValueAsString(info);
            return new ResponseDTO(GOOGLE.name().toLowerCase(), rawJson);
        } catch (Exception e) {
            throw new InvalidTokenException(INVALID_GOOGLE_TOKEN.getMessage(), e);
        }
    }

    public static ResponseDTO facebookProvider(String token) {
        try {
            String url = FACEBOOK.getEndpoint() + token;
            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response);

            Map<String, Object> info = Map.of(
                    "email", json.path("email").asText(null),
                    "name", json.path("name").asText(null),
                    "picture", json.path("picture").path("data").path("url").asText(null),
                    "id", json.path("id").asText()
            );

            String rawJson = mapper.writeValueAsString(info);
            return new ResponseDTO(FACEBOOK.name().toLowerCase(), rawJson);
        } catch (Exception e) {
            throw new InvalidTokenException(INVALID_FACEBOOK_TOKEN.getMessage(), e);
        }
    }


}
