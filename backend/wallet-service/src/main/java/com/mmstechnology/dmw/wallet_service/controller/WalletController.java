package com.mmstechnology.dmw.wallet_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("/wallet")
public class WalletController {

    @GetMapping("/")
    public String getWallet() {
        return "Wallet Service";
    }

    @PostMapping("/{id}")
    public String createWallet(String id) {
        log.info("Creating wallet for user with id: {}", id);
        return "123";

    }
}
