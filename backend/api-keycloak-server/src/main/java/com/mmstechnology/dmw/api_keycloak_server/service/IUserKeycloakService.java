package com.mmstechnology.dmw.api_keycloak_server.service;

import com.mmstechnology.dmw.api_keycloak_server.model.dto.CompositeUserDTO;

import java.util.List;
import java.util.Optional;

public interface IUserKeycloakService {


    //List<CompositeUserDTO> searchUserByUsername(String username);

    Optional<CompositeUserDTO> createUser(CompositeUserDTO userDTO);

}
