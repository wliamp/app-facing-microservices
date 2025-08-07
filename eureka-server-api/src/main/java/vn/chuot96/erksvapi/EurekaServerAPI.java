package vn.chuot96.erksvapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerAPI {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerAPI.class, args);
	}

}
