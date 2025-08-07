package vn.chuot96.verify3rd.util;

import static vn.chuot96.verify3rd.constant.Message.INVALID_FACEBOOK_TOKEN;
import static vn.chuot96.verify3rd.constant.Message.INVALID_GOOGLE_TOKEN;
import static vn.chuot96.verify3rd.constant.Provider.FACEBOOK;
import static vn.chuot96.verify3rd.constant.Provider.GOOGLE;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.client.RestTemplate;
import vn.chuot96.verify3rd.dto.UserDTO;
import vn.chuot96.verify3rd.exception.InvalidTokenException;

public class OauthHandler {
    private static final RestTemplate restTemplate = new RestTemplate();

    public static UserDTO googleProvider(String token) {
        try {
            JwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(GOOGLE.getUrl()).build();
            Jwt jwt = decoder.decode(token);
            return new UserDTO(GOOGLE.getKey(), jwt.getSubject());
        } catch (Exception e) {
            throw new InvalidTokenException(INVALID_GOOGLE_TOKEN.getMsg(), e);
        }
    }

    public static UserDTO facebookProvider(String token) {
        try {
            String url = FACEBOOK.getUrl() + token;
            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response);
            return new UserDTO(FACEBOOK.getKey(), json.path("id").asText());
        } catch (Exception e) {
            throw new InvalidTokenException(INVALID_FACEBOOK_TOKEN.getMsg(), e);
        }
    }
}
