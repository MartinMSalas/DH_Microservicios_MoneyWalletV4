package com.mmstechnology.dmw.wallet_service.controller;

import com.mmstechnology.dmw.wallet_service.model.dto.WalletDto;
import com.mmstechnology.dmw.wallet_service.service.IWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final IWalletService walletService;

    public WalletController(IWalletService walletService) {
        this.walletService = walletService;
    }


    @GetMapping("/")
    public String getWallet() {
        return "Wallet Service";
    }

    @PostMapping("/{userId}")
    public ResponseEntity<WalletDto> createWallet(@PathVariable String userId) {
        log.info("Creating wallet for user with id: {}", userId);
        WalletDto walletDto = walletService.createWallet(userId);

        return ResponseEntity.ok(walletDto);

    }

    @GetMapping("/alias")
    public String getAlias() {
        return "1234567890-1234567890-1234567890-123";
    }

    @GetMapping("/cvu")
    public String getCvu() {
        return "1234567890-1234567890-1234567890-123";
    }
}
