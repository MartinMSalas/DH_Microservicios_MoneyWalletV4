package com.mmstechnology.dmw.api_keycloak_server.repository;

import com.mmstechnology.dmw.api_keycloak_server.model.DatabaseUser;
import com.mmstechnology.dmw.api_keycloak_server.model.KeycloakUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserKeycloakRepository extends CrudRepository<DatabaseUser, String> {



}
