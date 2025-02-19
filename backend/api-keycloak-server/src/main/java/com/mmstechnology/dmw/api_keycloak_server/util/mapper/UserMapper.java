package com.mmstechnology.dmw.api_keycloak_server.util.mapper;

import com.mmstechnology.dmw.api_keycloak_server.model.KeycloakUser;
import com.mmstechnology.dmw.api_keycloak_server.model.dto.CompositeUserDTO;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class to map Keycloak UserRepresentation to UserDTO and vice versa.
 */
public class UserMapper {

    private UserMapper() {
        // Private constructor to prevent instantiation
    }

    /**
     * Converts a UserRepresentation to UserDTO.
     * @param userRepresentation The Keycloak user object.
     * @return UserDTO object.
     */
    public static KeycloakUser toUserDTO(UserRepresentation userRepresentation, Set<String> roles) {
        return KeycloakUser.builder()
                .userId(userRepresentation.getId())
                .username(userRepresentation.getUsername())
                .email(userRepresentation.getEmail())
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .roles(roles) // ✅ Now setting roles
                .password(null) // Passwords are never retrieved from Keycloak for security reasons
                .build();
    }

    /**
     * Converts a list of UserRepresentation to a list of UserDTOs.
     * @param userRepresentations List of Keycloak UserRepresentation.
     * @return List of UserDTOs.
     */
    public static List<KeycloakUser> toUserDTOList(List<UserRepresentation> userRepresentations, List<Set<String>> rolesList) {
        return userRepresentations.stream()
                .map(user -> toUserDTO(user, rolesList.get(userRepresentations.indexOf(user))))
                .collect(Collectors.toList());
    }

    /**
     * Converts a UserDTO to a UserRepresentation.
     * @param userDTO The UserDTO object.
     * @return UserRepresentation object.
     */
    public static UserRepresentation toUserRepresentation(KeycloakUser userDTO) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userDTO.username());
        userRepresentation.setEmail(userDTO.email());
        userRepresentation.setFirstName(userDTO.firstName());
        userRepresentation.setLastName(userDTO.lastName());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);
        return userRepresentation;
    }

    /**
     * Extracts roles from a UserRepresentation.
     * @param userRepresentation The Keycloak user object.
     * @return Set of role names.
     */
    private static Set<String> extractRoles(UserRepresentation userRepresentation) {
        return userRepresentation.getRealmRoles() != null ? Set.copyOf(userRepresentation.getRealmRoles()) : Set.of();
    }
}

