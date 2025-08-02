package vn.chuot96.jwtissapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import vn.chuot96.jwtissapi.component.HeaderValueAllowed;

@SpringBootApplication
@EnableConfigurationProperties(HeaderValueAllowed.class)
public class TokenIssuerAPI {

	public static void main(String[] args) {
		SpringApplication.run(TokenIssuerAPI.class, args);
	}

}