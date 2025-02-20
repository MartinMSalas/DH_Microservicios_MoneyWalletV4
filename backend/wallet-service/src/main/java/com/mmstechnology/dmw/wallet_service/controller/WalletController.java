package com.mmstechnology.dmw.wallet_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/wallet")
public class WalletController {

    @GetMapping("/")
    public String getWallet() {
        return "Wallet Service";
    }

    @PostMapping("/{id}")
    public String createWallet(@PathVariable String id) {
        log.info("Creating wallet for user with id: {}", id);
        return id;

    }
}
