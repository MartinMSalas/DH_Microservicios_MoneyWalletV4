package com.mmstechnology.dmw.api_keycloak.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('admin')")
    public String adminHi() {
        return "Hi Admin!";
    }

    @PreAuthorize("hasAnyRole('user', 'admin')")
    @GetMapping("/user")
    public String userHi() {
        return "Hi User!";
    }

}
