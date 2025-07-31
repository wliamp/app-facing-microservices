package vn.chuot96.authen3rdAPI.component;

import vn.chuot96.authen3rdAPI.dto.UserDTO;

public interface OauthProvider {
    String getProviderName();

    UserDTO verifyToken(String token);
}
