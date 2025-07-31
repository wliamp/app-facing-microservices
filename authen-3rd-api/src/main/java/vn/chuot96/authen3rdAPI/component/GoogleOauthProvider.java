package vn.chuot96.authen3rdAPI.component;

import org.springframework.stereotype.Component;
import vn.chuot96.authen3rdAPI.dto.ResponseDTO;
import vn.chuot96.authen3rdAPI.util.OauthHandler;

import static vn.chuot96.authen3rdAPI.constant.AuthProvider.GOOGLE;

@Component("google")
public class GoogleOauthProvider implements OauthProvider {
    @Override
    public String getProviderName() {
        return GOOGLE.getKey();
    }

    @Override
    public ResponseDTO verifyToken(String token) {
        return OauthHandler.googleProvider(token);
    }
}
