package com.mmstechnology.dmw.api_keycloak_server.service;

import com.mmstechnology.dmw.api_keycloak_server.model.dto.CompositeUserDTO;

import java.util.List;

public interface IKeycloakService {

    List<CompositeUserDTO> findAllUsers();
    List<CompositeUserDTO> searchUserByUsername(String username);

    String createUser(CompositeUserDTO userDTO);
    void deleteUser(String userId);
    void updateUser(String userId, CompositeUserDTO userDTO);
}
