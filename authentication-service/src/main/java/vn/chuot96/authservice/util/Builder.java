package vn.chuot96.authservice.util;

import java.util.List;
import java.util.Map;
import vn.chuot96.authservice.model.Aud;
import vn.chuot96.authservice.model.Scope;

public class Builder {
    public static Map<String, Object> buildTokenExtraClaims(List<Scope> scopes, List<Aud> auds) {
        return Map.of(
                "scope", Parser.parseScope(scopes),
                "audiences", Parser.parseAudience(auds));
    }
}
