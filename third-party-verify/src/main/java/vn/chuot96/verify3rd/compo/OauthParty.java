package vn.chuot96.verify3rd.compo;

import vn.chuot96.verify3rd.dto.User;

public interface OauthParty {
    String getName();

    User verify(String token);
}
