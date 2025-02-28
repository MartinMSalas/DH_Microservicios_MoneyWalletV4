package com.mmstechnology.dmw.api_keycloak_server.controller;

import com.mmstechnology.dmw.api_keycloak_server.exception.UserAlreadyExistsException;
import com.mmstechnology.dmw.api_keycloak_server.exception.UserCreationException;
import com.mmstechnology.dmw.api_keycloak_server.exception.UserNotFoundException;
import com.mmstechnology.dmw.api_keycloak_server.model.KeycloakUser;
import com.mmstechnology.dmw.api_keycloak_server.model.dto.CompositeUserDTO;
import com.mmstechnology.dmw.api_keycloak_server.service.IKeycloakService;
import com.mmstechnology.dmw.api_keycloak_server.util.KeycloakProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/keycloak/user")
//@PreAuthorize("hasRole('admin_client_role')")

@PreAuthorize("hasRole('admin_client_role')")
public class KeycloakController {

    private final IKeycloakService keycloakService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public KeycloakController(IKeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @GetMapping
    public ResponseEntity<?> findAllUsers() {
        log.info("Fetching all users from Keycloak...");

        List<KeycloakUser> userDTOList = keycloakService.findAllUsers();

        if (userDTOList.isEmpty()) {
            log.warn("No users found in Keycloak.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("No users available.");
        }

        log.info("Returning {} user(s).", userDTOList.size());
        return ResponseEntity.ok(userDTOList);
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> findUserByUsername(@PathVariable String username) {
        log.info("Searching for user with username: {}", username);

        List<KeycloakUser> users = keycloakService.searchUserByUsername(username);

        if (users.isEmpty()) {
            log.warn("No user found with username: {}", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with username '" + username + "' not found.");
        }

        log.info("Returning {} user(s) for username: {}", users.size(), username);
        return ResponseEntity.ok(users);
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

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody CompositeUserDTO userDTO) {
        log.info("Updating user with ID: {}", userId);

        try {
            keycloakService.updateUser(userId, userDTO);
            log.info("User with ID {} updated successfully.", userId);
            return ResponseEntity.ok("User updated successfully.");
        } catch (UserNotFoundException e) {
            log.warn("Update failed: User with ID {} not found.", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID '" + userId + "' not found.");
        } catch (Exception e) {
            log.error("Error updating user with ID {}.", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update user: " + e.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        log.info("Attempting to delete user with ID: {}", userId);

        try {
            keycloakService.deleteUser(userId);
            log.info("User with ID {} deleted successfully.", userId);
            return ResponseEntity.noContent().build(); // 204 NO CONTENT (Success but no response body)
        } catch (UserNotFoundException e) {
            log.warn("Delete failed: User with ID {} not found.", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID '" + userId + "' not found.");
        } catch (Exception e) {
            log.error("Error deleting user with ID {}.", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete user: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String email, @RequestParam String password) {
        log.info("Attempting to login user with email: {}", email);

        try {
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(KeycloakProvider.SERVER_URL)
                    .realm(KeycloakProvider.REALM_NAME)
                    .clientId(KeycloakProvider.ADMIN_CLI)
                    .username(email)
                    .password(password)
                    .build();

            AccessTokenResponse tokenResponse = keycloak.tokenManager().getAccessToken();

            String jwtToken = Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();

            return ResponseEntity.ok().body("{\"token\": \"" + jwtToken + "\"}");
        } catch (Exception e) {
            log.error("Login failed for user with email: {}", email, e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }
    }
}
