package vn.chuot96.authen3rdAPI.component;

import org.springframework.stereotype.Component;
import vn.chuot96.authen3rdAPI.dto.UserDTO;
import vn.chuot96.authen3rdAPI.util.OtpHandler;

import static vn.chuot96.authen3rdAPI.constant.AuthProvider.FIREBASE;

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
