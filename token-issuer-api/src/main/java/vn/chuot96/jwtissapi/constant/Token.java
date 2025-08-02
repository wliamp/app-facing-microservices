package vn.chuot96.jwtissapi.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Token {

    ACCESS_TOKEN("""
                    {"sub":"%s",
                    "iss":"%s",
                    "iat":%d,
                    "exp":%d,
                    "jti":"%s",
                    "scope":"%s",
                    "aud":"%s"}""",
            3600),

    REFRESH_TOKEN("""
            {
              "sub":"%s",
              "iss":"%s",
              "iat":%d,
              "exp":%d,
              "jti":"%s"
            }""",
            604800);

    // --> more Token type here

    private final String pattern;
    private final long duration;

}
