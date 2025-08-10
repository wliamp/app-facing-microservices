package vn.chuot96.jwtiss.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Token {
    ACCESS_EXP_SECONDS(3600), // 1 hour
    REFRESH_EXP_SECONDS(2592000), // 30 days
    REFRESH_THRESHOLD_SECONDS(86400), // 1 day
    REFRESH_TTL_DAYS(7);

    // --> more Token type here

    private final long duration;
}
