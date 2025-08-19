package vn.chuot96.authservice.util;

import java.util.List;
import java.util.Map;
import vn.chuot96.authservice.entity.Aud;
import vn.chuot96.authservice.entity.Scope;

public class Builder {
    public static Map<String, Object> buildTokenExtraClaims(List<Scope> scopes, List<Aud> auds) {
        return Map.of(
                "scope", Parser.parseScope(scopes),
                "aud", Parser.parseAudience(auds));
    }
}
