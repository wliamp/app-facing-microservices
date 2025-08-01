package vn.chuot96.verify3rdapi.component;

import org.springframework.stereotype.Component;
import vn.chuot96.verify3rdapi.dto.UserDTO;
import vn.chuot96.verify3rdapi.util.OauthHandler;

import static vn.chuot96.verify3rdapi.constant.AuthProvider.FACEBOOK;

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
