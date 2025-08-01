package vn.chuot96.auth3rdapi.component;

import org.springframework.stereotype.Component;
import vn.chuot96.auth3rdapi.dto.UserDTO;
import vn.chuot96.auth3rdapi.util.OauthHandler;

import static vn.chuot96.auth3rdapi.constant.AuthProvider.FACEBOOK;

@Component("facebook")
public class FacebookOauthProvider implements OauthProvider {

    @Override
    public String getProviderName() {
        return FACEBOOK.getKey();
    }

    @Override
    public UserDTO verifyToken(String token) {
        return OauthHandler.facebookProvider(token);
    }
}
