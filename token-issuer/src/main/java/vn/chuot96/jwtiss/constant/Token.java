package vn.chuot96.jwtiss.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Token {
    ACCESS_TOKEN("access", 3600), // 1 hour
    REFRESH_TOKEN("refresh", 2592000); // 30 days

    // --> more Token type here

    private final String type;
    private final long duration;
}
