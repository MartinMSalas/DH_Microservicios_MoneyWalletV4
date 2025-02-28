package com.mmstechnology.dmw.api_keycloak_server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmstechnology.dmw.api_keycloak_server.model.dto.CompositeUserDTO;
import com.mmstechnology.dmw.api_keycloak_server.service.IKeycloakService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserKeycloakController.class)
public class UserKeycloakControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IKeycloakService keycloakService;

    @InjectMocks
    private UserKeycloakController userKeycloakController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserById() throws Exception {
        CompositeUserDTO userDTO = new CompositeUserDTO("1", "testuser", "test@example.com", "Test", "User", null);
        when(keycloakService.getUserById(anyString())).thenReturn(userDTO);

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(new ObjectMapper().writeValueAsString(userDTO)));
    }

    @Test
    public void testUpdateUser() throws Exception {
        CompositeUserDTO userDTO = new CompositeUserDTO("1", "testuser", "test@example.com", "Test", "User", null);
        when(keycloakService.updateUser(anyString(), any(CompositeUserDTO.class))).thenReturn(Optional.of(userDTO));

        mockMvc.perform(patch("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("User updated successfully."));
    }
}
