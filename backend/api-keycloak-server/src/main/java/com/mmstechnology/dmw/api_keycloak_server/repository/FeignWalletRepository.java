package com.mmstechnology.dmw.api_keycloak_server.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "wallet-service")
public interface FeignWalletRepository {

    @PostMapping(value = "/wallet/{userId}")
    ResponseEntity<String> createWallet(@PathVariable("userId") String userId);
}
