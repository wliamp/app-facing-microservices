package io.wliamp.cko.util;

public class Extractor {
    public static String extractToken(String token, int size) {
        int start = token.indexOf(':') + 1;
        return token.substring(start, Math.min(start + size, token.length()));
    }
}
