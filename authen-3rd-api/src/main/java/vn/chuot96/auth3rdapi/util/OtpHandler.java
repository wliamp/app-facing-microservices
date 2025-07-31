package vn.chuot96.auth3rdapi.util;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import vn.chuot96.auth3rdapi.exception.InvalidTokenException;

import static vn.chuot96.auth3rdapi.constant.AuthMessage.*;
import static vn.chuot96.auth3rdapi.constant.AuthProvider.*;

public class OtpHandler {

    public static String firebaseProvider(String token) {
        return decodePhoneNumber(token, FIREBASE.getEndpoint());
    }

    // --> more OTP auth here

    private static String decodePhoneNumber(String token, String issuer) {
        try {
            JwtDecoder decoder = JwtDecoders.fromIssuerLocation(issuer);
            Jwt jwt = decoder.decode(token);

            String phoneNumber = jwt.getClaimAsString("phone_number");
            if (phoneNumber == null) {
                throw new InvalidTokenException(INVALID_OTP_PHONE.getMessage());
            }
            return phoneNumber;
        } catch (Exception ex) {
            throw new InvalidTokenException(INVALID_OTP_ENDPOINT.getMessage(), ex);
        }
    }
}
