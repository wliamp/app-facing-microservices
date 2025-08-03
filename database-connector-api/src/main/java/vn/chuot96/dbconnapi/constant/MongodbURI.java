package vn.chuot96.dbconnapi.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.chuot96.dbconnapi.dto.NosqlRequestDTO;

@Getter
@RequiredArgsConstructor
public enum MongodbURI {
    MONGODB("mongodb://%s:%s@%s:%s");

    private final String pattern;

    public String setUri(NosqlRequestDTO request) {
        return String.format(
                pattern, request.getUsername(), request.getPassword(), request.getHost(), request.getPort());
    }
}
