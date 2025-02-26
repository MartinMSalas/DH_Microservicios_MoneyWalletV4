package com.mmstechnology.dmw.api_keycloak_server.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.Set;


@Builder
public record CompositeUserDTO(String userId, String username, String email, String firstName, String lastName,
                               String password, String phoneNumber, Blob image, LocalDateTime lastLogin,
                               Integer failedAttempts, String walletId, Set<String> roles) {

}