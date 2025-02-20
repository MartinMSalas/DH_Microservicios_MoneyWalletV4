package com.mmstechnology.dmw.api_keycloak_server.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "wallet-service")
public interface FeignWalletRepository {
    @PostMapping(value = "/wallets")
    ResponseEntity<String> createWallet(@RequestParam String userId);
}