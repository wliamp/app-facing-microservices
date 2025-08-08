package vn.chuot96.authservice.compo;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "internal.header-allowed")
public class HeaderValueAllowed {
    private List<String> values = new ArrayList<>();

    public boolean isAllowed(String token) {
        return values.contains(token);
    }
}
