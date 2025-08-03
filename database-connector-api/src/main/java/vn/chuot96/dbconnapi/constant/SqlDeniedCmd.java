package vn.chuot96.dbconnapi.constant;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SqlDeniedCmd {
    DROP("drop"),
    TRUNCATE("truncate"),
    SHUTDOWN("shutdown"),
    GRANT("grant"),
    REVOKE("revoke"),
    CREATE_USER("create user"),
    DROP_USER("drop user"),
    KILL("kill"),
    FLUSH("flush"),
    LOAD_DATA("load data"),
    ALTER_USER("alter user");

    public static final Set<String> COMMANDS =
            Stream.of(values()).map(SqlDeniedCmd::getCmd).collect(Collectors.toSet());
    private final String cmd;

    public static boolean isDenied(String input) {
        if (input == null) return false;
        String lowerInput = input.toLowerCase();
        return COMMANDS.stream().anyMatch(lowerInput::contains);
    }

    public static Optional<String> findFirstMatch(String input) {
        if (input == null) return Optional.empty();
        String lowerInput = input.toLowerCase(Locale.ROOT);
        return COMMANDS.stream().filter(lowerInput::contains).findFirst();
    }
}
