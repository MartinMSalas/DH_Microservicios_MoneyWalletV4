package com.mmstechnology.dmw.api_keycloak_server.controller;

import com.mmstechnology.dmw.api_keycloak_server.exception.UserAlreadyExistsException;
import com.mmstechnology.dmw.api_keycloak_server.exception.UserCreationException;
import com.mmstechnology.dmw.api_keycloak_server.model.dto.CompositeUserDTO;
import com.mmstechnology.dmw.api_keycloak_server.service.IKeycloakService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequestMapping("/user/register")
//@PreAuthorize("hasRole('admin_client_role')")
//@PreAuthorize("hasRole('admin_client_role')")
public class UserKeycloakController {


    private final IKeycloakService keycloakService;

    public UserKeycloakController(IKeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CompositeUserDTO userDTO) {
        log.info("Creating user: {}", userDTO.username());

        try {
            String response = keycloakService.createUser(userDTO);
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
    }

}
