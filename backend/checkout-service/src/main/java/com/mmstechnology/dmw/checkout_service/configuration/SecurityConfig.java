package com.mmstechnology.dmw.checkout_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .authorizeHttpRequests(authorize->authorize
                        .requestMatchers("/actuator/**", "/eureka/**").permitAll()
                        .requestMatchers("/checkout/{id}").hasAuthority("SCOPE_email")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwkSetUri("http://localhost:9090/realms/DigitalMoneyWallet/protocol/openid-connect/certs")
                        )
                );
        return httpSecurity.build();

    }
}
