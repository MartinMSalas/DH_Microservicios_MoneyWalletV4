package com.mmstechnology.dmw.api_keycloak_server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiKeycloakApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void testLoginWithValidCredentials() {
        // Arrange
        String email = "validuser@example.com";
        String password = "validpassword";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("email", email);
        requestBody.add("password", password);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity("/keycloak/user/login", requestBody, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("token");
    }

    @Test
    void testLoginWithInvalidCredentials() {
        // Arrange
        String email = "invaliduser@example.com";
        String password = "invalidpassword";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("email", email);
        requestBody.add("password", password);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity("/keycloak/user/login", requestBody, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void testAccessProtectedEndpointWithValidToken() {
        // Arrange
        String email = "validuser@example.com";
        String password = "validpassword";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("email", email);
        requestBody.add("password", password);

        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/keycloak/user/login", requestBody, String.class);
        String token = loginResponse.getBody().split(":")[1].replace("\"", "").replace("}", "").trim();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Act
        ResponseEntity<String> response = restTemplate.exchange("/keycloak/user/protected-endpoint", HttpMethod.GET, entity, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testAccessProtectedEndpointWithInvalidToken() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer invalidtoken");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Act
        ResponseEntity<String> response = restTemplate.exchange("/keycloak/user/protected-endpoint", HttpMethod.GET, entity, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void testLogoutWithValidToken() {
        // Arrange
        String email = "validuser@example.com";
        String password = "validpassword";
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("email", email);
        requestBody.add("password", password);

        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/keycloak/user/login", requestBody, String.class);
        String token = loginResponse.getBody().split(":")[1].replace("\"", "").replace("}", "").trim();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Act
        ResponseEntity<String> response = restTemplate.exchange("/keycloak/user/logout", HttpMethod.POST, entity, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testLogoutWithInvalidToken() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer invalidtoken");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Act
        ResponseEntity<String> response = restTemplate.exchange("/keycloak/user/logout", HttpMethod.POST, entity, String.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void testNewFunctionalitySprint2() {
        ResponseEntity<String> response = restTemplate.getForEntity("/new-functionality/test", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testUpdatedFunctionalitySprint1() {
        ResponseEntity<String> response = restTemplate.getForEntity("/updated-functionality/test", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testNewFunctionalitySprint3() {
        ResponseEntity<String> response = restTemplate.getForEntity("/new-functionality-sprint3/test", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testUpdatedFunctionalitySprint3() {
        ResponseEntity<String> response = restTemplate.getForEntity("/updated-functionality-sprint3/test", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
