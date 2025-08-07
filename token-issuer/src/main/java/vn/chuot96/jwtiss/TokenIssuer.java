package vn.chuot96.jwtiss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import vn.chuot96.jwtiss.compo.HeaderValueAllowed;

@SpringBootApplication
@EnableConfigurationProperties(HeaderValueAllowed.class)
public class TokenIssuer {
    public static void main(String[] args) {
        SpringApplication.run(TokenIssuer.class, args);
    }
}
