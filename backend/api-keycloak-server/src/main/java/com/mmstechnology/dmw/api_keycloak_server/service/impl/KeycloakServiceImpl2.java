package com.mmstechnology.dmw.api_keycloak_server.service.impl;

import com.mmstechnology.dmw.api_keycloak_server.exception.UserAlreadyExistsException;
import com.mmstechnology.dmw.api_keycloak_server.exception.UserCreationException;
import com.mmstechnology.dmw.api_keycloak_server.model.dto.CompositeUserDTO;
import com.mmstechnology.dmw.api_keycloak_server.service.IKeycloakServiceV0;
import com.mmstechnology.dmw.api_keycloak_server.util.KeycloakProvider;
import jakarta.annotation.Nonnull;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of Keycloak user management service.
 * <p>
 * ✅ Follows best practices:
 * - Modular helper methods for DTO mapping, user ID extraction, credential creation, and role assignment.
 * - Structured exception handling with detailed logging.
 * - Uses `Set<>` for efficient role lookup.
 * - Ensures **high maintainability** and **performance optimization**.
 */

@Slf4j
public class KeycloakServiceImpl2 implements IKeycloakServiceV0 {


    private static final boolean ENABLE_USER_IMMEDIATELY = true;
    private static final String DEFAULT_ROLE = "user";

    @Override
    public List<UserRepresentation> findAllUsers() {
        log.info("Fetching all users from Keycloak...");
        return KeycloakProvider.getRealmResource().users().list();
    }

    @Override
    public List<UserRepresentation> searchUserByUsername(String username) {
        log.info("Searching users with username: {}", username);
        return KeycloakProvider.getRealmResource().users().searchByUsername(username, true);
    }

    @Override
    public String createUser(@Nonnull CompositeUserDTO userDTO) {
        log.info("Creating new user: {}", userDTO.username());

        UsersResource usersResource = KeycloakProvider.getUserResource();
        UserRepresentation userRepresentation = createUserRepresentation(userDTO);

        Response response = usersResource.create(userRepresentation);

        if (response.getStatus() == Response.Status.CONFLICT.getStatusCode()) {
            throw new UserAlreadyExistsException("User already exists: " + userDTO.username());
        } else if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
            throw new UserCreationException("Failed to create user, status: " + response.getStatus());
        }

        String userId = extractUserId(response);
        log.info("User created successfully with ID: {}", userId);

        setUserPassword(userId, userDTO.password());
        assignRolesToUser(userId, userDTO.roles());

        return "User created successfully!";
    }

    @Override
    public void deleteUser(String userId) {
        log.info("Deleting user with ID: {}", userId);
        try {
            KeycloakProvider.getUserResource().get(userId).remove();
            log.info("User {} deleted successfully", userId);
        } catch (Exception e) {
            log.error("Failed to delete user with ID: {}", userId, e);
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    @Override
    public void updateUser(String userId, @Nonnull CompositeUserDTO userDTO) {
        log.info("Updating user with ID: {}", userId);

        UserResource userResource = KeycloakProvider.getUserResource().get(userId);
        UserRepresentation updatedUserRepresentation = createUserRepresentation(userDTO);

        if (userDTO.password() != null && !userDTO.password().isBlank()) {
            setUserPassword(userId, userDTO.password());
        }

        userResource.update(updatedUserRepresentation);
        log.info("User {} updated successfully", userId);
    }

    /**
     * ✅ Maps a `UserDTO` to `UserRepresentation`.
     */
    private UserRepresentation createUserRepresentation(CompositeUserDTO userDTO) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userDTO.username());
        userRepresentation.setEmail(userDTO.email());
        userRepresentation.setFirstName(userDTO.firstName());
        userRepresentation.setLastName(userDTO.lastName());
        userRepresentation.setEnabled(ENABLE_USER_IMMEDIATELY);
        userRepresentation.setEmailVerified(ENABLE_USER_IMMEDIATELY);
        return userRepresentation;
    }

    /**
     * ✅ Sets the password for a given user.
     */
    private void setUserPassword(String userId, String password) {
        log.info("Setting password for user ID: {}", userId);
        CredentialRepresentation credential = createCredential(password);
        UserResource userResource = KeycloakProvider.getUserResource().get(userId);
        userResource.resetPassword(credential);
    }

    /**
     * ✅ Creates a password credential representation.
     */
    private CredentialRepresentation createCredential(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(OAuth2Constants.PASSWORD);
        credential.setValue(password);
        return credential;
    }

    /**
     * ✅ Assigns roles to a user. If no roles are provided, assigns the default role.
     */
    /**
     * ✅ Assigns roles to a user. If no roles are provided, assigns the default role.
     */
    private void assignRolesToUser(String userId, Set<String> roles) {
        log.info("Assigning roles {} to user ID: {}", roles, userId);

        RealmResource realmResource = KeycloakProvider.getRealmResource();
        List<RoleRepresentation> roleRepresentations;

        if (roles == null || roles.isEmpty()) {
            RoleRepresentation defaultRole = realmResource.roles().get(DEFAULT_ROLE).toRepresentation();
            roleRepresentations = List.of(defaultRole);
        } else {
            roleRepresentations = roles.stream()
                    .map(roleName -> {
                        try {
                            return realmResource.roles().get(roleName).toRepresentation();
                        } catch (Exception e) {
                            log.warn("Role {} not found in Keycloak", roleName);
                            return null;
                        }
                    })
                    .filter(role -> role != null)
                    .collect(Collectors.toList());
        }

        if (!roleRepresentations.isEmpty()) {
            realmResource.users().get(userId).roles().realmLevel().add(roleRepresentations);
            log.info("Assigned roles [{}] to user {}", roleRepresentations.stream().map(RoleRepresentation::getName).collect(Collectors.joining(", ")), userId);
        } else {
            log.warn("No matching roles found to assign to user ID: {}", userId);
        }
    }

    /**
     * ✅ Extracts the user ID from Keycloak's response.
     */
    private String extractUserId(Response response) {
        return Optional.ofNullable(response.getLocation())
                .map(loc -> loc.getPath().replaceAll(".*/([^/]+)$", "$1"))
                .orElseThrow(() -> new UserCreationException("Unable to extract user ID from response location."));
    }
}