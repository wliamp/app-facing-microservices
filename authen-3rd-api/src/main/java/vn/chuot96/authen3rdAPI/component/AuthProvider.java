package vn.chuot96.authen3rdAPI.component;

import org.springframework.security.oauth2.jwt.Jwt;
import vn.chuot96.authen3rdAPI.dto.UserDTO;
import vn.chuot96.authen3rdAPI.exception.InvalidTokenException;

public interface AuthProvider {
    String getProviderName();
    UserDTO verifyToken(Jwt jwt) throws InvalidTokenException;
}
