package vn.chuot96.authen3rdAPI.component;

import vn.chuot96.authen3rdAPI.dto.ResponseDTO;

public interface OtpProvider {
    boolean supports(String token);
    ResponseDTO verifyToken(String token);
}
