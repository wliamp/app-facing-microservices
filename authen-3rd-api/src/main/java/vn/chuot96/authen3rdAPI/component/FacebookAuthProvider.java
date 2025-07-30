package vn.chuot96.authen3rdAPI.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import vn.chuot96.authen3rdAPI.dto.UserDTO;
import vn.chuot96.authen3rdAPI.exception.InvalidTokenException;

import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

@Component("facebook")
public class FacebookAuthProvider implements AuthProvider {

    private static final String GRAPH_API_URL = "https://graph.facebook.com/me?fields=id,name,email,picture&access_token=";

    @Override
    public String getProviderName() {
        return "facebook";
    }

    @Override
    public UserDTO verifyToken(Jwt jwt) throws InvalidTokenException {
        String token = jwt.getTokenValue();
        try {
            URL url = new URL(GRAPH_API_URL + token);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(conn.getInputStream());

            return new UserDTO(
                    json.has("email") ? json.get("email").asText() : null,
                    json.get("name").asText(),
                    json.get("picture").get("data").get("url").asText(),
                    json.get("id").asText()
            );
        } catch (Exception e) {
            throw new InvalidTokenException("Facebook token is invalid", e);
        }
    }
}
