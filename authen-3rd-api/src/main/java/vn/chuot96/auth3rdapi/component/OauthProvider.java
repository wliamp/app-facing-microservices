package vn.chuot96.auth3rdapi.component;

import vn.chuot96.auth3rdapi.dto.UserDTO;

public interface OauthProvider {
    String getProviderName();

    UserDTO verifyToken(String token);
}
