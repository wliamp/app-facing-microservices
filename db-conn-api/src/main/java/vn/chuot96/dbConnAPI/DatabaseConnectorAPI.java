package vn.chuot96.dbConnAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
		org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration.class,
		org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration.class
})
public class DatabaseConnectorAPI {

	public static void main(String[] args) {
		SpringApplication.run(DatabaseConnectorAPI.class, args);
	}

}
