package com.mmstechnology.dmw.api_keycloak_server.model.dto;

import lombok.Builder;

import java.util.Set;


@Builder
public record CompositeUserDTO(String userId, String username, String email, String firstName, String lastName, String password,
                               Set<String> roles) {

}
