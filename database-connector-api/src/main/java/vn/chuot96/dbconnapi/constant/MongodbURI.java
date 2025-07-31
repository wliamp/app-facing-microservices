package vn.chuot96.dbconnapi.constant;

import vn.chuot96.dbconnapi.dto.NosqlRequestDTO;

public enum MongodbURI {
    PATTERN("mongodb://%s:%s@%s:%s");

    private final String pattern;

    MongodbURI(String pattern) {
        this.pattern = pattern;
    }

    public String setUri(NosqlRequestDTO request) {
        return String.format(pattern, request.getUsername(), request.getPassword(), request.getHost(), request.getPort());
    }
}
