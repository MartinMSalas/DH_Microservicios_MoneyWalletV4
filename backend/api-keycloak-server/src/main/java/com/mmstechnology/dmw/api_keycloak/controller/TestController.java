package com.mmstechnology.dmw.api_keycloak.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/admin")
    public String adminHi() {
        return "Hi Admin!";
    }

    @GetMapping("/user")
    public String userHi() {
        return "Hi User!";
    }

}
