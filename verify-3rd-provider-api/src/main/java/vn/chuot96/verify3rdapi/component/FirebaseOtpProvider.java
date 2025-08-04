package vn.chuot96.verify3rdapi.component;

import static vn.chuot96.verify3rdapi.constant.Provider.FIREBASE;

import org.springframework.stereotype.Component;
import vn.chuot96.verify3rdapi.dto.UserDTO;
import vn.chuot96.verify3rdapi.util.OtpHandler;

@Component
public class FirebaseOtpProvider implements OtpProvider {

    @Override
    public boolean supports(String token) {
        return token != null && token.startsWith(FIREBASE.getKey());
    }

    @Override
    public UserDTO verifyToken(String token) {
        return new UserDTO(FIREBASE.getKey(), OtpHandler.firebaseProvider(token));
    }
}
