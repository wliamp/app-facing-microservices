package io.wliamp.cko.util;

import java.util.UUID;

public class Generator {
    public static String generateCode(int size) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, size);
    }
}
