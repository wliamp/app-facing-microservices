package vn.chuot96.authen3rdAPI.component;

import vn.chuot96.authen3rdAPI.dto.UserDTO;

public interface OtpProvider {
    boolean supports(String token);
    UserDTO verifyToken(String token);
}
