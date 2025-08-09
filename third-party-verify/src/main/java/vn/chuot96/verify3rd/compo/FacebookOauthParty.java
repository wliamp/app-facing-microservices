package vn.chuot96.verify3rd.compo;

import static vn.chuot96.verify3rd.constant.Party.FACEBOOK;

import org.springframework.stereotype.Component;
import vn.chuot96.verify3rd.dto.User;
import vn.chuot96.verify3rd.util.OauthHandler;

@Component("facebook")
public class FacebookOauthParty implements OauthParty {

    @Override
    public String getName() {
        return FACEBOOK.getKey();
    }

    @Override
    public User verify(String token) {
        return OauthHandler.facebookParty(token);
    }
}
