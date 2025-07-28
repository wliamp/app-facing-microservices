package vn.chuot96.tokenIssuerAPI.constant;

public enum AccessToken {
    PATTERN("""
            {"token_type":"access",
            "sub":"%s",
            "iss":"token-issuer-api",
            "iat":%d,"exp":%d,
            "jti":"%s",
            "scope":"%s",
            "aud":"%s"}""",
            3600);

    private final String pattern;
    private final long duration;

    AccessToken(String pattern, long duration) {
        this.pattern = pattern;
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }


    public String getPattern() {
        return pattern;
    }
}
