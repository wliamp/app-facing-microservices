package vn.chuot96.verify3rd.compo;

import vn.chuot96.verify3rd.dto.User;

public interface OtpParty {
    boolean supports(String token);

    User verify(String token);
}
