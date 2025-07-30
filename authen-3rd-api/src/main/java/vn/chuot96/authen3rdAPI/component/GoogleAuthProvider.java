package vn.chuot96.authen3rdAPI.component;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import vn.chuot96.authen3rdAPI.dto.UserDTO;
import vn.chuot96.authen3rdAPI.exception.InvalidTokenException;

@Component("google")
public class GoogleAuthProvider implements AuthProvider {
    @Override
    public String getProviderName() {
        return "google";
    }

    @Override
    public UserDTO verifyToken(Jwt jwt) throws InvalidTokenException {
        try {
            return new UserDTO(
                    jwt.getClaimAsString("email"),
                    jwt.getClaimAsString("name"),
                    jwt.getClaimAsString("picture"),
                    jwt.getSubject()
            );
        } catch (NullPointerException | ClassCastException e) {
            throw new InvalidTokenException("JWT does not contain required claims or has invalid format.", e);
        }
    }
}
