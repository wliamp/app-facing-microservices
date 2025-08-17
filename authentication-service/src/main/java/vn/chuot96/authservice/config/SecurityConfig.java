package vn.chuot96.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {
    //    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .authorizeExchange(ex -> ex.pathMatchers("/auth/**")
//                        .authenticated()
//                        .anyExchange()
//                        .permitAll())
//                .build();
//    }
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(ex -> ex.anyExchange().permitAll()) // cho phép tất cả
                .build();
    }

}
