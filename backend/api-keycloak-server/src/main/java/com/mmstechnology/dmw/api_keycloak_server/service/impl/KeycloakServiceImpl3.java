package com.mmstechnology.dmw.api_keycloak_server.service.impl;



import com.mmstechnology.dmw.api_keycloak_server.exception.UserAlreadyExistsException;
import com.mmstechnology.dmw.api_keycloak_server.exception.UserCreationException;
import com.mmstechnology.dmw.api_keycloak_server.model.dto.UserDTO;
import com.mmstechnology.dmw.api_keycloak_server.service.IKeycloakService;
import com.mmstechnology.dmw.api_keycloak_server.util.KeycloakProvider;
import com.mmstechnology.dmw.api_keycloak_server.util.mapper.UserMapper;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KeycloakServiceImpl3 implements IKeycloakService {

    private static final boolean ENABLE_USER_IMMEDIATELY = true;
    private static final String DEFAULT_ROLE = "user";

    @Override
    public List<UserDTO> findAllUsers() {
        log.info("Fetching all users from Keycloak...");
        RealmResource realm = KeycloakProvider.getRealmResource();
        List<UserRepresentation> users = realm.users().list();

        List<Set<String>> userRoles = users.stream()
                .map(user -> realm.users().get(user.getId()).roles().realmLevel().listEffective()
                        .stream().map(RoleRepresentation::getName).collect(Collectors.toSet()))
                .collect(Collectors.toList());

        return UserMapper.toUserDTOList(users, userRoles);
    }

    @Override
    public List<UserDTO> searchUserByUsername(String username) {
        log.info("Searching users with username: {}", username);
        RealmResource realm = KeycloakProvider.getRealmResource();
        List<UserRepresentation> users = realm.users().searchByUsername(username, true);

        List<Set<String>> userRoles = users.stream()
                .map(user -> realm.users().get(user.getId()).roles().realmLevel().listEffective()
                        .stream().map(RoleRepresentation::getName).collect(Collectors.toSet()))
                .collect(Collectors.toList());

        return UserMapper.toUserDTOList(users, userRoles);
    }

    @Override
    public String createUser(@Nonnull UserDTO userDTO) {
        log.info("Creating new user: {}", userDTO.username());

        UsersResource usersResource = KeycloakProvider.getUserResource();
        UserRepresentation userRepresentation = UserMapper.toUserRepresentation(userDTO);

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
        KeycloakProvider.getUserResource().get(userId).remove();
        log.info("User {} deleted successfully", userId);
    }

    @Override
    public void updateUser(String userId, @Nonnull UserDTO userDTO) {
        log.info("Updating user with ID: {}", userId);

        UserResource userResource = KeycloakProvider.getUserResource().get(userId);
        UserRepresentation updatedUserRepresentation = UserMapper.toUserRepresentation(userDTO);

        if (userDTO.password() != null && !userDTO.password().isBlank()) {
            setUserPassword(userId, userDTO.password());
        }

        userResource.update(updatedUserRepresentation);
        log.info("User {} updated successfully", userId);
    }

    private void setUserPassword(String userId, String password) {
        log.info("Setting password for user ID: {}", userId);
        CredentialRepresentation credential = createCredential(password);
        UserResource userResource = KeycloakProvider.getUserResource().get(userId);
        userResource.resetPassword(credential);
    }

    private CredentialRepresentation createCredential(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(OAuth2Constants.PASSWORD);
        credential.setValue(password);
        return credential;
    }

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

    private String extractUserId(Response response) {
        return Optional.ofNullable(response.getLocation())
                .map(loc -> loc.getPath().replaceAll(".*/([^/]+)$", "$1"))
                .orElseThrow(() -> new UserCreationException("Unable to extract user ID from response location."));
    }
}
