package vn.chuot96.dbconnapi.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.chuot96.dbconnapi.dto.NosqlRequestDTO;

@Getter
@RequiredArgsConstructor
public enum MongodbURI {
    MONGODB("mongodb://%s%s@%s:%s/%s%s", "authSource=admin&retryWrites=true&w=majority");

    private final String pattern;

    private final String options;

    public String setUri(NosqlRequestDTO request) {
        String username = request.db().username();
        String password = request.db().password();
        String host = request.db().host();
        String port = request.db().port();
        String database = request.db().dbName();
        String userInfo = (username != null && password != null) ? username + ":" + password : "";
        String atSign = userInfo.isEmpty() ? "" : "@";
        String optionsStr = (options != null && !options.isBlank()) ? "?" + options : "";
        return String.format(pattern, userInfo, atSign, host, port, database, optionsStr);
    }
}
