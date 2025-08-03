package vn.chuot96.verify3rdapi.util;

import static vn.chuot96.verify3rdapi.constant.AuthMessage.*;
import static vn.chuot96.verify3rdapi.constant.AuthProvider.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.client.RestTemplate;
import vn.chuot96.verify3rdapi.dto.UserDTO;
import vn.chuot96.verify3rdapi.exception.InvalidTokenException;

public class OauthHandler {

    private static final RestTemplate restTemplate = new RestTemplate();

    public static UserDTO googleProvider(String token) {
        try {
            JwtDecoder decoder =
                    NimbusJwtDecoder.withJwkSetUri(GOOGLE.getEndpoint()).build();
            Jwt jwt = decoder.decode(token);
            return new UserDTO(GOOGLE.getKey(), jwt.getSubject());
        } catch (Exception e) {
            throw new InvalidTokenException(INVALID_GOOGLE_TOKEN.getMessage(), e);
        }
    }

    public static UserDTO facebookProvider(String token) {
        try {
            String url = FACEBOOK.getEndpoint() + token;
            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response);
            return new UserDTO(FACEBOOK.getKey(), json.path("id").asText());
        } catch (Exception e) {
            throw new InvalidTokenException(INVALID_FACEBOOK_TOKEN.getMessage(), e);
        }
    }
}
