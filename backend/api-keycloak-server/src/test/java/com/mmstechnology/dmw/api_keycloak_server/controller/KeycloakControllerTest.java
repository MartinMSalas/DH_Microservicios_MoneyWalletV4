package com.mmstechnology.dmw.api_keycloak_server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmstechnology.dmw.api_keycloak_server.model.dto.UserDTO;
import com.mmstechnology.dmw.api_keycloak_server.service.IKeycloakService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KeycloakController.class)
public class KeycloakControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IKeycloakService keycloakService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testRegisterUserWithValidData() throws Exception {
        UserDTO userDTO = new UserDTO("1", "testuser", "testuser@example.com", "Test", "User", "password", Set.of("user"));
        when(keycloakService.registerUser(any(UserDTO.class))).thenReturn("User registered successfully!");

        mockMvc.perform(post("/keycloak/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully!"));
    }

    @Test
    public void testRegisterUserWithInvalidData() throws Exception {
        UserDTO userDTO = new UserDTO("1", "", "testuser@example.com", "Test", "User", "password", Set.of("user"));

        mockMvc.perform(post("/keycloak/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid or incomplete user data."));
    }

    @Test
    public void testRegisterUserWithAlreadyRegisteredUser() throws Exception {
        UserDTO userDTO = new UserDTO("1", "testuser", "testuser@example.com", "Test", "User", "password", Set.of("user"));
        doThrow(new UserAlreadyExistsException("User already exists")).when(keycloakService).registerUser(any(UserDTO.class));

        mockMvc.perform(post("/keycloak/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isConflict())
                .andExpect(content().string("User 'testuser' already exists."));
    }

    @Test
    public void testRegisterUserWithServerError() throws Exception {
        UserDTO userDTO = new UserDTO("1", "testuser", "testuser@example.com", "Test", "User", "password", Set.of("user"));
        doThrow(new UserCreationException("Internal server error")).when(keycloakService).registerUser(any(UserDTO.class));

        mockMvc.perform(post("/keycloak/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to register user: Internal server error"));
    }
}
