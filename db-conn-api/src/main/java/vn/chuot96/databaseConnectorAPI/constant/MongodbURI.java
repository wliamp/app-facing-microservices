package vn.chuot96.databaseConnectorAPI.constant;

import org.springframework.web.bind.annotation.RequestBody;
import vn.chuot96.databaseConnectorAPI.dto.NosqlRequestDTO;

public enum MongodbURI {
    PATTERN("mongodb://%s:%s@%s:%s");

    private final String pattern;

    MongodbURI(String pattern) {
        this.pattern = pattern;
    }

    public String setUri(@RequestBody NosqlRequestDTO request) {
        return String.format(pattern, request.getUsername(), request.getPassword(), request.getHost(), request.getPort());
    }
}
