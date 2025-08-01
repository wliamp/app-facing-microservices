package vn.chuot96.verify3rdapi.component;

import vn.chuot96.verify3rdapi.dto.UserDTO;

public interface OtpProvider {
    boolean supports(String token);
    UserDTO verifyToken(String token);
}
