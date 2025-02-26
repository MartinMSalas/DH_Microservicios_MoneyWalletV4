package com.mmstechnology.dmw.api_keycloak_server.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mmstechnology.dmw.api_keycloak_server.exception.UserAlreadyExistsException;
import com.mmstechnology.dmw.api_keycloak_server.exception.UserCreationException;
import com.mmstechnology.dmw.api_keycloak_server.model.DatabaseUser;
import com.mmstechnology.dmw.api_keycloak_server.model.KeycloakUser;
import com.mmstechnology.dmw.api_keycloak_server.model.dto.CompositeUserDTO;
import com.mmstechnology.dmw.api_keycloak_server.repository.UserKeycloakRepository;
import com.mmstechnology.dmw.api_keycloak_server.service.IKeycloakService;
import com.mmstechnology.dmw.api_keycloak_server.service.IUserKeycloakService;
import com.mmstechnology.dmw.api_keycloak_server.service.IWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserKeycloakServiceImpl implements IUserKeycloakService {

    private final IKeycloakService keycloakService;
    private final IWalletService walletService;
    private final UserKeycloakRepository userKeycloakRepository;

    public UserKeycloakServiceImpl(IKeycloakService keycloakService, IWalletService walletService, UserKeycloakRepository userKeycloakRepository) {
        this.keycloakService = keycloakService;
        this.walletService = walletService;
        this.userKeycloakRepository = userKeycloakRepository;
    }

    @Override
    public Optional<CompositeUserDTO> createUser(CompositeUserDTO userDTO)  {


        String response = keycloakService.createUser(userDTO);
        // Check optional value
        if (response == null) {
            log.error("User creation failed in keycloak");
            throw new UserCreationException("User creation failed in keycloak");
        }
        List<KeycloakUser> kUsers = keycloakService.searchUserByUsername(userDTO.username());

        if (kUsers.isEmpty()) {
            log.error("User creation failed in keycloak");
            throw new UserCreationException("User creation failed in keycloak");
        }
        log.info("User {} created successfully in keycloak.", userDTO.username());
        String userId = kUsers.get(0).userId();
        String walletId = walletService.createWallet(userId);

        // Use builder to construct DatabaseUser
        DatabaseUser dbUser = DatabaseUser.builder()
                .userId(userId)
                .walletId(walletId)
                .build();
        if (userDTO.phoneNumber() != null){
            dbUser.setPhoneNumber(userDTO.phoneNumber());
        }else{
            dbUser.setPhoneNumber("");
        }

        userKeycloakRepository.save(dbUser);

        log.info("User {} created successfully in database.", userDTO.username());

        CompositeUserDTO compositeUserDTO = CompositeUserDTO.builder()
                .userId(userId)
                .phoneNumber(dbUser.getPhoneNumber())
                .walletId(walletId)
                .username(userDTO.username())
                .email(userDTO.email())
                .firstName(userDTO.firstName())
                .lastName(userDTO.lastName())
                .password(userDTO.password())
                .roles(kUsers.get(0).roles())
                .build();


        return Optional.of(compositeUserDTO);
    }


}
