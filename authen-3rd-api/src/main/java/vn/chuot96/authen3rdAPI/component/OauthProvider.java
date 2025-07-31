package vn.chuot96.authen3rdAPI.component;

import vn.chuot96.authen3rdAPI.dto.ResponseDTO;

public interface OauthProvider {
    String getProviderName();
    ResponseDTO verifyToken(String token);
}
