package vn.chuot96.verify3rd.compo;

import org.springframework.stereotype.Component;
import vn.chuot96.verify3rd.dto.UserDTO;
import vn.chuot96.verify3rd.util.OauthHandler;

import static vn.chuot96.verify3rd.constant.Provider.FACEBOOK;

@Component("facebook")
public class FacebookOauthParty implements OauthParty {

    @Override
    public String getProviderName() {
        return FACEBOOK.getKey();
    }

    @Override
    public UserDTO verifyToken(String token) {
        return OauthHandler.facebookProvider(token);
    }
}
