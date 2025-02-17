package com.mmstechnology.dmw.api_keycloak_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ApiKeycloakApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiKeycloakApplication.class, args);
	}

}
