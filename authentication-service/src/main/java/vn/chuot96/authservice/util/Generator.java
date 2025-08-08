package vn.chuot96.authservice.util;

import java.util.UUID;

public class Generator {
    public static String generateCode() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .replaceAll("[^A-Za-z0-9]", "")
                .substring(0, 8)
                .toUpperCase();
    }
}
