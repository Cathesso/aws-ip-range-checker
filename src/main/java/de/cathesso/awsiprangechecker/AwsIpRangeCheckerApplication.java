package de.cathesso.awsiprangechecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class AwsIpRangeCheckerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsIpRangeCheckerApplication.class, args);
	}

    @Bean
    public RestTemplate createRestTemplate() {
        return new RestTemplate();
    }

}
