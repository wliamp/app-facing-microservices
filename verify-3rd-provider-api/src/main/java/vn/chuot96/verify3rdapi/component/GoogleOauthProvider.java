package vn.chuot96.verify3rdapi.component;

import org.springframework.stereotype.Component;
import vn.chuot96.verify3rdapi.dto.UserDTO;
import vn.chuot96.verify3rdapi.util.OauthHandler;

import static vn.chuot96.verify3rdapi.constant.AuthProvider.GOOGLE;

@Component("google")
public class GoogleOauthProvider implements OauthProvider {
    @Override
    public String getProviderName() {
        return GOOGLE.getKey();
    }

    @Override
    public UserDTO verifyToken(String token) {
        return OauthHandler.googleProvider(token);
    }
}
