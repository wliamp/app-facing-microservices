package vn.chuot96.verify3rd.util;

import static vn.chuot96.verify3rd.constant.Message.INVALID_OTP_ENDPOINT;
import static vn.chuot96.verify3rd.constant.Message.INVALID_OTP_PHONE;
import static vn.chuot96.verify3rd.constant.Party.FIREBASE;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import vn.chuot96.verify3rd.exception.InvalidTokenException;

public class OtpHandler {
    public static String firebaseProvider(String token) {
        return decodePhoneNumber(token, FIREBASE.getUrl());
    }

    private static String decodePhoneNumber(String token, String issuer) {
        try {
            JwtDecoder decoder = JwtDecoders.fromIssuerLocation(issuer);
            Jwt jwt = decoder.decode(token);
            String phoneNumber = jwt.getClaimAsString("phone_number");
            if (phoneNumber == null) {
                throw new InvalidTokenException(INVALID_OTP_PHONE.getMsg());
            }
            return phoneNumber;
        } catch (Exception ex) {
            throw new InvalidTokenException(INVALID_OTP_ENDPOINT.getMsg(), ex);
        }
    }
}
