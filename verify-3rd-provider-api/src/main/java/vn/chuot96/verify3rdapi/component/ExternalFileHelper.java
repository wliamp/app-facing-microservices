package vn.chuot96.verify3rdapi.component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExternalFileHelper {
    private final Map<String, String> data = new HashMap<>();

    @Value("${external-file.path}")
    private String path;

    public String getString(String key) {
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            return stream.filter(line -> line.startsWith(key + "="))
                    .map(line -> line.substring(key.length() + 1).trim())
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read external file: " + path, e);
        }
    }

    public List<String> getList(String key) {
        String raw = getString(key);
        return (raw != null) ? Arrays.stream(raw.split(",")).map(String::trim).toList() : List.of();
    }
}
