package vn.chuot96.auth3rdapi.component;

import org.springframework.stereotype.Component;
import vn.chuot96.auth3rdapi.dto.UserDTO;
import vn.chuot96.auth3rdapi.util.OtpHandler;

import static vn.chuot96.auth3rdapi.constant.AuthProvider.FIREBASE;

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
