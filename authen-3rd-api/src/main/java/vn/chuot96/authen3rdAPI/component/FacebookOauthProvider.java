package vn.chuot96.authen3rdAPI.component;

import org.springframework.stereotype.Component;
import vn.chuot96.authen3rdAPI.dto.ResponseDTO;
import vn.chuot96.authen3rdAPI.util.OauthHandler;

import static vn.chuot96.authen3rdAPI.constant.AuthProvider.FACEBOOK;

@Component("facebook")
public class FacebookOauthProvider implements OauthProvider {

    @Override
    public String getProviderName() {
        return FACEBOOK.getKey();
    }

    @Override
    public ResponseDTO verifyToken(String token) {
        return OauthHandler.facebookProvider(token);
    }
}
