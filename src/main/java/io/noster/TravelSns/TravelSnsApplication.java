package io.noster.TravelSns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class TravelSnsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelSnsApplication.class, args);
	}

}
