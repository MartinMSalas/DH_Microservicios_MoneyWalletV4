package com.mmstechnology.dmw.wallet_service.controller;

import com.mmstechnology.dmw.wallet_service.model.dto.WalletDto;
import com.mmstechnology.dmw.wallet_service.model.dto.TransactionDto;
import com.mmstechnology.dmw.wallet_service.service.IWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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

    @GetMapping("/accounts/{id}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String id) {
        log.info("Fetching balance for account with id: {}", id);
        BigDecimal balance = walletService.getBalance(id);
        return ResponseEntity.ok(balance);
    }

    @GetMapping("/accounts/{id}/transactions")
    public ResponseEntity<List<TransactionDto>> getLast5Transactions(@PathVariable String id) {
        log.info("Fetching last 5 transactions for account with id: {}", id);
        List<TransactionDto> transactions = walletService.getLast5Transactions(id);
        return ResponseEntity.ok(transactions);
    }
}
