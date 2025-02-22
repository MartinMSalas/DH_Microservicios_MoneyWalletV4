package com.mmstechnology.dmw.api_keycloak_server.model;

import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@EntityScan
public record CompositeUser(String userId, String username, String email, String firstName, String lastName,
                                String password, String phoneNumber, Blob image, LocalDateTime lastLogin,
                                Integer failedAttempts, String walletId, Set<String> roles) {

}
