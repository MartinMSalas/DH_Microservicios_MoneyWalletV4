package com.mmstechnology.dmw.api_keycloak_server.service.impl;

import com.mmstechnology.dmw.api_keycloak_server.model.dto.UserDTO;
import com.mmstechnology.dmw.api_keycloak_server.service.IKeycloakService;
import com.mmstechnology.dmw.api_keycloak_server.util.KeycloakProvider;
import jakarta.annotation.Nonnull;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Collections;
import java.util.List;

public class KeycloakServiceImpl implements IKeycloakService {

    /**
     * Method to find all user of Keycloak
     * @return List<UserRepresentation>
     */
    @Override
    public List<UserRepresentation> findAllUsers() {
        return KeycloakProvider.getRealmResource()
                .users()
                .list();

    }

    /**
     * Method to search user by username
     *
     * @param username
     * @return List<UserRepresentation>
     */
    @Override
    public List<UserRepresentation> searchUserByUsername(String username) {
        return KeycloakProvider.getRealmResource()
                .users()
                .searchByUsername(username,true);


    }

    /**
     * Method to create user
     *
     * @param userDTO
     * @return String
     */
    @Override
    public String createUser(@Nonnull UserDTO userDTO) {
        int status = 0;
        UsersResource userResource = KeycloakProvider.getUserResource();
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userDTO.username());
        userRepresentation.setEmail(userDTO.email());
        userRepresentation.setFirstName(userDTO.firstName());
        userRepresentation.setLastName(userDTO.lastName());
        // Determine based on business needs: if immediate user activation is desired, set true;
        // otherwise, it remains false to allow for email verification or admin review.
        boolean enableUserImmediately = true; // Change this flag if needed.
        userRepresentation.setEnabled(enableUserImmediately);
        userRepresentation.setEmailVerified(enableUserImmediately);

        Response response = userResource.create(userRepresentation);
        if (response.getStatus() == 201) {
            String path = response.getLocation().getPath();
            // Extract the created user ID from the location header using a regex.
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(OAuth2Constants.PASSWORD);
            credentialRepresentation.setValue(userDTO.password());

            UserResource user = userResource.get(userId);
            user.resetPassword(credentialRepresentation);

            RealmResource realmResource = KeycloakProvider.getRealmResource();

            List<RoleRepresentation> roleRepresentations = null;

            if(userDTO.roles() == null || userDTO.roles().isEmpty()){
                roleRepresentations = List.of(realmResource.roles().get("user").toRepresentation());

            } else{
                roleRepresentations = realmResource.roles()
                        .list()
                        .stream()
                        .filter(role -> userDTO.roles()
                                .stream()
                                .anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName())))
                        .toList();
                realmResource.users()
                        .get(userId)
                        .roles()
                        .realmLevel()
                        .add(roleRepresentations);

                return "User created successfully !";
            }

        } else if(status==409){

            return "User already exist";


        }
        else {
            throw new RuntimeException("Failed to create user: " + response.getStatusInfo());
        }

        return "";
    }

    /**
     * Method to delete user
     * @param userId
     */
    @Override
    public void deleteUser(String userId) {
        KeycloakProvider.getUserResource()
                .get(userId)
                .remove();
    }

    /**
     * Method to update user
     *
     * @param userId
     * @param userDTO
     */
    @Override
    public void updateUser(String userId,@Nonnull UserDTO userDTO) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setValue(userDTO.password());

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userDTO.username());
        userRepresentation.setEmail(userDTO.email());
        userRepresentation.setFirstName(userDTO.firstName());
        userRepresentation.setLastName(userDTO.lastName());

        // Determine based on business needs: if immediate user activation is desired, set true;
        // otherwise, it remains false to allow for email verification or admin review.
        boolean enableUserImmediately = true; // Change this flag if needed.
        userRepresentation.setEnabled(enableUserImmediately);
        userRepresentation.setEmailVerified(enableUserImmediately);

        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));

        UserResource userResource = KeycloakProvider.getUserResource().get(userId);
        userResource.update(userRepresentation);
    }
}
