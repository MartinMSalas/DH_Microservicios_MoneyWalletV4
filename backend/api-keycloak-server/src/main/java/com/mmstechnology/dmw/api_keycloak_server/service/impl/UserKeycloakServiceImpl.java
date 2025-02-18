package com.mmstechnology.dmw.api_keycloak_server.service.impl;

import com.mmstechnology.dmw.api_keycloak_server.exception.UserAlreadyExistsException;
import com.mmstechnology.dmw.api_keycloak_server.exception.UserCreationException;
import com.mmstechnology.dmw.api_keycloak_server.model.dto.CompositeUserDTO;
import com.mmstechnology.dmw.api_keycloak_server.repository.UserKeycloakRepository;
import com.mmstechnology.dmw.api_keycloak_server.service.IKeycloakService;
import com.mmstechnology.dmw.api_keycloak_server.service.IUserKeycloakService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Slf4j
@Service
public class UserKeycloakServiceImpl implements IUserKeycloakService {

    private final IKeycloakService keycloakService;
    private final UserKeycloakRepository userKeycloakRepository;

    public UserKeycloakServiceImpl(IKeycloakService keycloakService, UserKeycloakRepository userKeycloakRepository) {
        this.keycloakService = keycloakService;
        this.userKeycloakRepository = userKeycloakRepository;
    }

    @Override
    public Optional<CompositeUserDTO> createUser(CompositeUserDTO userDTO) {
        try {
            String response = keycloakService.createUser(userDTO);
            // Check optional value
            log.info("User {} created successfully.", userDTO.username());



            return ResponseEntity.created(new URI("/users/" + userDTO.username()))
                    .body(response);
        } catch (UserAlreadyExistsException e) {
            log.warn("User creation failed: User {} already exists.", userDTO.username());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User '" + userDTO.username() + "' already exists.");
        } catch (UserCreationException e) {
            log.error("User creation failed due to internal error.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create user: " + e.getMessage());
        } catch (URISyntaxException e) {
            log.error("URI Syntax Exception while creating user.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred.");
        }

        return Optional.empty();
    }


}
