package vn.chuot96.verify3rd.compo;

import static vn.chuot96.verify3rd.constant.Party.FIREBASE;

import org.springframework.stereotype.Component;
import vn.chuot96.verify3rd.dto.User;
import vn.chuot96.verify3rd.util.OtpHandler;

@Component
public class FirebaseOtpParty implements OtpParty {
    @Override
    public boolean supports(String token) {
        return token != null && token.startsWith(FIREBASE.getKey());
    }

    @Override
    public User verify(String token) {
        return new User(FIREBASE.getKey(), OtpHandler.firebaseProvider(token));
    }
}
