package vn.chuot96.authen3rdAPI.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.chuot96.authen3rdAPI.component.OauthProvider;
import vn.chuot96.authen3rdAPI.dto.UserDTO;
import vn.chuot96.authen3rdAPI.exception.NoSupportedProviderException;

import java.util.List;

import static vn.chuot96.authen3rdAPI.constant.AuthMessage.NOT_FOUND_OAUTH;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final List<OauthProvider> providers;

    public UserDTO verifyToken(String provider, String token) {
        return providers.stream()
                .filter(p -> p.getProviderName().equalsIgnoreCase(provider))
                .findFirst()
                .map(p -> p.verifyToken(token))
                .orElseThrow(() -> new NoSupportedProviderException(NOT_FOUND_OAUTH.getMessage()));
    }

}
