package com.mmstechnology.dmw.api_keycloak_server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mmstechnology.dmw.api_keycloak_server.exception.UserAlreadyExistsException;
import com.mmstechnology.dmw.api_keycloak_server.exception.UserCreationException;
import com.mmstechnology.dmw.api_keycloak_server.exception.UserNotFoundException;
import com.mmstechnology.dmw.api_keycloak_server.model.dto.CompositeUserDTO;
import com.mmstechnology.dmw.api_keycloak_server.service.IKeycloakService;
import com.mmstechnology.dmw.api_keycloak_server.service.IUserKeycloakService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/user")
//@PreAuthorize("hasRole('admin_client_role')")
//@PreAuthorize("hasRole('admin_client_role')")
public class UserKeycloakController {


    //private final IKeycloakService keycloakService;
    private final IUserKeycloakService userKeycloakService;
    private final IKeycloakService keycloakService;

    public UserKeycloakController(IKeycloakService keycloakService, IUserKeycloakService userKeycloakService) {
        this.userKeycloakService = userKeycloakService;
        this.keycloakService = keycloakService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody CompositeUserDTO userDTO) {
        log.info("Creating user: {}", userDTO.username());

        try {
            Optional<CompositeUserDTO> response = userKeycloakService.createUser(userDTO);
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
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        log.info("Fetching user with id: {}", id);

        try {
            CompositeUserDTO userDTO = keycloakService.getUserById(id);
            return ResponseEntity.ok(userDTO);
        } catch (UserNotFoundException e) {
            log.warn("User with id {} not found.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with id '" + id + "' not found.");
        } catch (Exception e) {
            log.error("Error fetching user with id {}.", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch user: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody CompositeUserDTO userDTO) {
        log.info("Updating user with id: {}", id);

        try {
            keycloakService.updateUser(id, userDTO);
            return ResponseEntity.ok("User updated successfully.");
        } catch (UserNotFoundException e) {
            log.warn("User with id {} not found.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with id '" + id + "' not found.");
        } catch (Exception e) {
            log.error("Error updating user with id {}.", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update user: " + e.getMessage());
        }
    }
}
