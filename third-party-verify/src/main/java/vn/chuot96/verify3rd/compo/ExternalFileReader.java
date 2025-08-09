package vn.chuot96.verify3rd.compo;

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
public class ExternalFileReader {
    private final Map<String, String> data = new HashMap<>();

    @Value("${external-file.path}")
    private String path;

    private boolean loaded = false;

    private void loadFileOnce() {
        if (!loaded) {
            try (Stream<String> stream = Files.lines(Paths.get(path))) {
                stream.forEach(line -> {
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        data.put(parts[0].trim(), parts[1].trim());
                    }
                });
                loaded = true;
            } catch (IOException e) {
                throw new RuntimeException("Cannot read external file: " + path, e);
            }
        }
    }

    public String getOne(String key) {
        loadFileOnce();
        return data.get(key);
    }

    public List<String> getMany(String key) {
        String raw = getOne(key);
        return (raw != null) ? Arrays.stream(raw.split(",")).map(String::trim).toList() : List.of();
    }
}
