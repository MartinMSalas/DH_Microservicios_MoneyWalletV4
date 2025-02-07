package com.mmstechnology.dmw.api_keycloak_server.model.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Set;


@Builder
public record UserDTO(String username, String email, String firstName, String lastName, String password,
                      Set<String> roles) {

}
