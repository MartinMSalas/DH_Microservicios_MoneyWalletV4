package com.mmstechnology.dmw.api_keycloak_server.service;

import com.mmstechnology.dmw.api_keycloak_server.model.dto.UserDTO;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface IKeycloakServiceV0 {

    List<UserRepresentation> findAllUsers();
    List<UserRepresentation> searchUserByUsername(String username);

    String createUser(UserDTO userDTO);
    void deleteUser(String userId);
    void updateUser(String userId, UserDTO userDTO);
}

