package com.mmstechnology.dmw.api_keycloak_server.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmstechnology.dmw.api_keycloak_server.repository.FeignWalletRepository;
import com.mmstechnology.dmw.api_keycloak_server.service.IWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WalletServiceImpl implements IWalletService {

    private final FeignWalletRepository feignWalletRepository;

    public WalletServiceImpl(FeignWalletRepository feignWalletRepository) {
        this.feignWalletRepository = feignWalletRepository;
    }

    @Override
    public String createWallet(String userId) {

        ResponseEntity<String> response = feignWalletRepository.createWallet(userId);
        String jsonResponse = response.getBody();

        if (jsonResponse != null) {
            ObjectMapper mapper = new ObjectMapper();

            JsonNode rootNode = null;
            try {
                rootNode = mapper.readTree(jsonResponse);
            } catch (JsonProcessingException e) {
                log.error("Error parsing JSON response: {}", e.getMessage());
                // Handle the error appropriately, e.g., return a default value or rethrow as a runtime exception
                return "error";  // or: throw new RuntimeException("Failed to parse walletId", e);
            }

            return rootNode.path("walletId").asText();
        }
        System.out.println(response.getBody());
        return "body response is null";
    }
}
