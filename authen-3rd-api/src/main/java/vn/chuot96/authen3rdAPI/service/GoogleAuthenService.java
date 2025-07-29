package vn.chuot96.authen3rdAPI.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import vn.chuot96.authen3rdAPI.dto.UserDTO;
import vn.chuot96.authen3rdAPI.exception.InvalidTokenException;

@Service
public class GoogleAuthenService {

    public static UserDTO verifyToken(Jwt jwt) {
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
