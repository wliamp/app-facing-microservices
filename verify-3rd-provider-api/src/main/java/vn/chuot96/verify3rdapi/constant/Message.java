package vn.chuot96.verify3rdapi.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Message {
    INVALID_GOOGLE_TOKEN("Google JWT does not contain required claims or has invalid format"),
    INVALID_FACEBOOK_TOKEN("Invalid Facebook raw"),
    INVALID_OTP_PHONE("Missing phone_number claim in raw"),
    INVALID_OTP_ENDPOINT("Failed to decode raw from Provider's endpoint"),
    NOT_FOUND_OAUTH("No OAuth Provider matched"),
    NOT_FOUND_OTP("No OTP Provider matched");

    private final String msg;
}
