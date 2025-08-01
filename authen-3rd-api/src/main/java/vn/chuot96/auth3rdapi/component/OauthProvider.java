package vn.chuot96.auth3rdapi.component;

import vn.chuot96.auth3rdapi.dto.TokenResponseDTO;

public interface OauthProvider {
    String getProviderName();
    TokenResponseDTO verifyToken(String token);
}
