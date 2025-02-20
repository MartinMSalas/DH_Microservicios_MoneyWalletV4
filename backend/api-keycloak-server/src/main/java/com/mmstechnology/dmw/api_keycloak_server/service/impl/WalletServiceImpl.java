package com.mmstechnology.dmw.api_keycloak_server.service.impl;

import com.mmstechnology.dmw.api_keycloak_server.repository.FeignWalletRepository;
import com.mmstechnology.dmw.api_keycloak_server.service.IWalletService;
import org.springframework.http.ResponseEntity;

public class WalletServiceImpl implements IWalletService {

    private final FeignWalletRepository feignWalletRepository;

    public WalletServiceImpl(FeignWalletRepository feignWalletRepository) {
        this.feignWalletRepository = feignWalletRepository;
    }

    @Override
    public String createWallet(String userId) {

        ResponseEntity<String> response = feignWalletRepository.createWallet(userId);
        System.out.println(response.getBody());
        return "123";
    }
}
