package com.mmstechnology.dmw.wallet_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WalletServiceApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void testWalletServiceHealthCheck() {
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/health", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("\"status\":\"UP\"");
    }

    @Test
    void testWalletServiceEnvEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity("/actuator/env", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("propertySources");
    }

    @Test
    void testWalletServiceRefreshEndpoint() {
        ResponseEntity<String> response = restTemplate.postForEntity("/actuator/refresh", null, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
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
}
