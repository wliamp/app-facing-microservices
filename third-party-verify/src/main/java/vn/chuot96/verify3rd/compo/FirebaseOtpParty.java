package vn.chuot96.verify3rd.compo;

import org.springframework.stereotype.Component;
import vn.chuot96.verify3rd.dto.UserDTO;
import vn.chuot96.verify3rd.util.OtpHandler;

import static vn.chuot96.verify3rd.constant.Provider.FIREBASE;

@Component
public class FirebaseOtpParty implements OtpParty {
    @Override
    public boolean supports(String token) {
        return token != null && token.startsWith(FIREBASE.getKey());
    }

    @Override
    public UserDTO verifyToken(String token) {
        return new UserDTO(FIREBASE.getKey(), OtpHandler.firebaseProvider(token));
    }
}
