package vn.chuot96.verify3rd.compo;

import vn.chuot96.verify3rd.dto.UserDTO;

public interface OauthParty {
    String getProviderName();

    UserDTO verifyToken(String token);
}
