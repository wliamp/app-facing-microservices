package vn.chuot96.authen3rdAPI.component;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import vn.chuot96.authen3rdAPI.dto.UserDTO;
import vn.chuot96.authen3rdAPI.exception.InvalidTokenException;

@Component("phone")
public class PhoneAuthProvider implements AuthProvider {
    @Override
    public String getProviderName() {
        return "phone";
    }

    @Override
    public UserDTO verifyToken(Jwt jwt) throws InvalidTokenException {
        try {
            return new UserDTO(
                    null,
                    jwt.getClaimAsString("phone_number"),
                    null,
                    jwt.getSubject()
            );
        } catch (NullPointerException | ClassCastException e) {
            throw new InvalidTokenException("Invalid JWT for phone login", e);
        }
    }
}
