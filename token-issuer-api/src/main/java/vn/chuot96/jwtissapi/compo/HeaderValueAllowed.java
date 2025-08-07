package vn.chuot96.jwtissapi.compo;

import java.util.List;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@RequiredArgsConstructor
public class HeaderValueAllowed {
    private final ExternalFileReader externalFileReader;

    private List<String> values;

    @PostConstruct
    public void init() {
        this.values = externalFileReader.list("JwtIssHeaderValuesAllowed");
    }

    public boolean isAllowed(String token) {
        return values.contains(token);
    }
}

