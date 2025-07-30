package vn.chuot96.authen3rdAPI.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import vn.chuot96.authen3rdAPI.component.AuthProvider;
import vn.chuot96.authen3rdAPI.dto.UserDTO;
import vn.chuot96.authen3rdAPI.exception.InvalidTokenException;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final Map<String, AuthProvider> providers;

    public UserDTO verifyToken(String providerName, Jwt jwt) {
        AuthProvider authProvider = providers.get(providerName.toLowerCase());
        if (authProvider == null) {
            throw new InvalidTokenException("Unsupported provider: " + providerName);
        }
        return authProvider.verifyToken(jwt);
    }

}
