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
    public ResponseEntity<WalletDto> getAccountInfo(@PathVariable String id) {
        log.info("Fetching account information for account with id: {}", id);
        WalletDto walletDto = walletService.getAccountInfo(id);
        return ResponseEntity.ok(walletDto);
    }

    @PatchMapping("/accounts/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable String id, @RequestBody WalletDto walletDto) {
        log.info("Updating account with id: {}", id);
        try {
            walletService.updateAccount(id, walletDto);
            return ResponseEntity.ok("Account updated successfully.");
        } catch (AccountNotFoundException e) {
            log.warn("Account with id {} not found.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Account with id '" + id + "' not found.");
        } catch (Exception e) {
            log.error("Error updating account with id {}.", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update account: " + e.getMessage());
        }
    }

    @GetMapping("/accounts/{id}/transactions")
    public ResponseEntity<List<TransactionDto>> getLast5Transactions(@PathVariable String id) {
        log.info("Fetching last 5 transactions for account with id: {}", id);
        List<TransactionDto> transactions = walletService.getLast5Transactions(id);
        return ResponseEntity.ok(transactions);
    }
}
