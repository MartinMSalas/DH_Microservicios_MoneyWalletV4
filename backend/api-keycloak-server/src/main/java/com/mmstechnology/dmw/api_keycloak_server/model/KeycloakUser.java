package com.mmstechnology.dmw.api_keycloak_server.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.Set;

@EntityScan
@Builder
public record KeycloakUser(String userId, String username, String email, String firstName, String lastName, String password,
                           Set<String> roles) {


}
