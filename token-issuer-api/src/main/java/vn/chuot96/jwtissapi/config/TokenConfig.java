package vn.chuot96.jwtissapi.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import vn.chuot96.jwtissapi.util.AccessTokenHandler;
import vn.chuot96.jwtissapi.util.RefreshTokenHandler;

@Configuration
public class TokenConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public JwtEncoder jwtEncoder() {
        SecretKey secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
    }

    @Bean
    public AccessTokenHandler accessTokenHandler() {
        return new AccessTokenHandler();
    }

    @Bean
    public RefreshTokenHandler refreshTokenHandler() {
        return new RefreshTokenHandler();
    }
}
