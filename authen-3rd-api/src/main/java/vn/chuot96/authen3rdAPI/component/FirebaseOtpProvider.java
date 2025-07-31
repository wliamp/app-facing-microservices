package vn.chuot96.authen3rdAPI.component;

import org.springframework.stereotype.Component;
import vn.chuot96.authen3rdAPI.dto.ResponseDTO;
import vn.chuot96.authen3rdAPI.util.OtpHandler;

import static vn.chuot96.authen3rdAPI.constant.AuthProvider.FIREBASE;

@Component
public class FirebaseOtpProvider implements OtpProvider {

    @Override
    public boolean supports(String token) {
        return token != null && token.startsWith(FIREBASE.getKey());
    }

    @Override
    public ResponseDTO verifyToken(String token) {
        return new ResponseDTO(FIREBASE.getKey(), OtpHandler.firebaseProvider(token));
    }

}
