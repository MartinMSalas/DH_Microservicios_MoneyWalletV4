package com.mmstechnology.dmw.api_gateway_server;

import com.mmstechnology.dmw.api_gateway_server.filters.CustomFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiGatewayServerApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void testCustomFilter() {
        ResponseEntity<String> response = restTemplate.getForEntity("/checkout/test", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getFirst("X-Request-from")).isEqualTo("Gateway");
    }

    @Test
    void testRouting() {
        ResponseEntity<String> response = restTemplate.getForEntity("/product/test", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Configuration
    static class TestConfig {

        @Bean
        public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
            return builder.routes()
                    .route("checkout-service", r -> r.path("/checkout/**")
                            .filters(f -> f.addRequestHeader("X-Request-from", "Gateway"))
                            .uri("lb://checkout-service"))
                    .route("product-service", r -> r.path("/product/**")
                            .filters(f -> f.addRequestHeader("X-Request-from", "Gateway"))
                            .uri("lb://product-service"))
                    .build();
        }

        @Bean
        public CustomFilter customFilter() {
            return new CustomFilter();
        }
    }
}
