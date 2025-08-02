package vn.chuot96.verify3rdapi.component;

import vn.chuot96.verify3rdapi.dto.UserDTO;

public interface OauthProvider {
    String getProviderName();
    UserDTO verifyToken(String token);
}
