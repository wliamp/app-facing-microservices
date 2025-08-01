package vn.chuot96.auth3rdapi.component;

import vn.chuot96.auth3rdapi.dto.TokenResponseDTO;

public interface OtpProvider {
    boolean supports(String token);
    TokenResponseDTO verifyToken(String token);
}
