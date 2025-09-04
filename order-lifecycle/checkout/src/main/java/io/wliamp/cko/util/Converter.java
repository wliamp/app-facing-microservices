package io.wliamp.cko.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Converter {
    public static String kebabToCamel(String input) {
        String[] parts = input.split("-");
        return Arrays.stream(parts)
                .map(part -> Arrays
                        .asList(parts)
                        .indexOf(part) == 0
                        ? part
                        : Character.toUpperCase(part.charAt(0))
                        + part.substring(1))
                .collect(Collectors.joining(""));
    }
}
