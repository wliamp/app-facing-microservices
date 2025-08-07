package vn.chuot96.verify3rd.compo;

import org.springframework.stereotype.Component;
import vn.chuot96.verify3rd.dto.UserDTO;
import vn.chuot96.verify3rd.util.OauthHandler;

import static vn.chuot96.verify3rd.constant.Provider.GOOGLE;

@Component("google")
public class GoogleOauthParty implements OauthParty {
    @Override
    public String getProviderName() {
        return GOOGLE.getKey();
    }

    @Override
    public UserDTO verifyToken(String token) {
        return OauthHandler.googleProvider(token);
    }
}
