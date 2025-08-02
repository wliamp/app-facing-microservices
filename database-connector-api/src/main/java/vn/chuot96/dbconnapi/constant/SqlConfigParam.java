package vn.chuot96.dbconnapi.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SqlConfigParam {

    MAXIMUM_POOL_SIZE(10),
    CONNECTION_TIMEOUT(5000),
    IDLE_TIMEOUT(30000),
    MAX_LIFE_TIME(600000),
    DEFAULT_SELECT_LIMIT(1000);

    private final long value;

}
