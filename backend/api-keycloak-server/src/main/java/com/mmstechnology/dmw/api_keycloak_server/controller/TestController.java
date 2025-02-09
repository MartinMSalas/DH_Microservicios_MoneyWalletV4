package com.mmstechnology.dmw.api_keycloak_server.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('admin_client_role')")
    public String adminHi() {
        return "Hi Admin!";
    }

    @PreAuthorize("hasAnyRole('user_client_role', 'admin_client_role')")
    @GetMapping("/user")
    public String userHi() {
        return "Hi User!";
    }

}
