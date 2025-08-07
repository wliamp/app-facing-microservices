package vn.chuot96.verify3rd.compo;

import vn.chuot96.verify3rd.dto.UserDTO;

public interface OtpParty {
    boolean supports(String token);

    UserDTO verifyToken(String token);
}
