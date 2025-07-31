package vn.chuot96.tokenIssuerAPI.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Token {

    ACCESS_TOKEN("access",
            """
                    {"sub":"%s",
                    "iss":"%s",
                    "iat":%d,
                    "exp":%d,
                    "jti":"%s",
                    "scope":"%s",
                    "aud":"%s"}
                    """,
            3600);

    private final String type;
    private final String pattern;
    private final long duration;

}
