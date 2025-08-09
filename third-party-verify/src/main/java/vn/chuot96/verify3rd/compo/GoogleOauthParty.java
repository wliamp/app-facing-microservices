package vn.chuot96.verify3rd.compo;

import static vn.chuot96.verify3rd.constant.Party.GOOGLE;

import org.springframework.stereotype.Component;
import vn.chuot96.verify3rd.dto.User;
import vn.chuot96.verify3rd.util.OauthHandler;

@Component("google")
public class GoogleOauthParty implements OauthParty {
    @Override
    public String getName() {
        return GOOGLE.getKey();
    }

    @Override
    public User verify(String token) {
        return OauthHandler.googleProvider(token);
    }
}
