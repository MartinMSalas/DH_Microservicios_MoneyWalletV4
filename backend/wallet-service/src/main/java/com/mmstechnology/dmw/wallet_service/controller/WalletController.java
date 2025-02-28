package com.mmstechnology.dmw.wallet_service.controller;

import com.mmstechnology.dmw.wallet_service.model.dto.WalletDto;
import com.mmstechnology.dmw.wallet_service.model.dto.TransactionDto;
import com.mmstechnology.dmw.wallet_service.service.IWalletService;
import com.mmstechnology.dmw.wallet_service.service.ICardService;
import com.mmstechnology.dmw.wallet_service.model.Card;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final IWalletService walletService;
    private final ICardService cardService;

    public WalletController(IWalletService walletService, ICardService cardService) {
        this.walletService = walletService;
        this.cardService = cardService;
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

    @GetMapping("/accounts/{accountId}/cards")
    public ResponseEntity<List<Card>> listCards(@PathVariable String accountId) {
        log.info("Listing cards for account with id: {}", accountId);
        List<Card> cards = cardService.listCards(accountId);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/accounts/{accountId}/cards/{cardId}")
    public ResponseEntity<Card> getCardDetails(@PathVariable String accountId, @PathVariable String cardId) {
        log.info("Fetching details for card with id: {} in account with id: {}", cardId, accountId);
        Card card = cardService.getCardDetails(accountId, cardId);
        return ResponseEntity.ok(card);
    }

    @PostMapping("/accounts/{accountId}/cards")
    public ResponseEntity<?> addCard(@PathVariable String accountId, @RequestBody Card card) {
        log.info("Adding card to account with id: {}", accountId);
        try {
            cardService.addCard(accountId, card);
            return ResponseEntity.status(HttpStatus.CREATED).body("Card added successfully.");
        } catch (CardAlreadyExistsException e) {
            log.warn("Card already exists in another account.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Card already exists in another account.");
        } catch (Exception e) {
            log.error("Error adding card to account with id {}.", accountId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add card: " + e.getMessage());
        }
    }

    @DeleteMapping("/accounts/{accountId}/cards/{cardId}")
    public ResponseEntity<?> deleteCard(@PathVariable String accountId, @PathVariable String cardId) {
        log.info("Deleting card with id: {} from account with id: {}", cardId, accountId);
        try {
            cardService.deleteCard(accountId, cardId);
            return ResponseEntity.ok("Card deleted successfully.");
        } catch (CardNotFoundException e) {
            log.warn("Card with id {} not found in account with id {}.", cardId, accountId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card not found.");
        } catch (Exception e) {
            log.error("Error deleting card with id {} from account with id {}.", cardId, accountId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete card: " + e.getMessage());
        }
    }

    @GetMapping("/accounts/{accountId}/activity")
    public ResponseEntity<List<TransactionDto>> getAllActivities(@PathVariable String accountId) {
        log.info("Fetching all activities for account with id: {}", accountId);
        List<TransactionDto> activities = walletService.getAllActivities(accountId);
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/accounts/{accountId}/activity/{transferenceID}")
    public ResponseEntity<TransactionDto> getActivityDetails(@PathVariable String accountId, @PathVariable Long transferenceID) {
        log.info("Fetching details for activity with id: {} in account with id: {}", transferenceID, accountId);
        TransactionDto activityDetails = walletService.getActivityDetails(accountId, transferenceID);
        return ResponseEntity.ok(activityDetails);
    }

    @PostMapping("/accounts/{accountId}/transferences")
    public ResponseEntity<?> registerMoneyIncome(@PathVariable String accountId, @RequestBody TransactionDto transactionDto) {
        log.info("Registering money income for account with id: {}", accountId);
        try {
            walletService.registerMoneyIncome(accountId, transactionDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Money income registered successfully.");
        } catch (InvalidTransactionException e) {
            log.warn("Invalid transaction data: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid transaction data: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error registering money income for account with id {}.", accountId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register money income: " + e.getMessage());
        }
    }

    @GetMapping("/accounts/{id}/transferences")
    public ResponseEntity<List<String>> getLastRecipients(@PathVariable String id) {
        log.info("Fetching last recipients for account with id: {}", id);
        try {
            List<String> recipients = walletService.getLastRecipients(id);
            return ResponseEntity.ok(recipients);
        } catch (AccountNotFoundException e) {
            log.warn("Account with id {} not found.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(List.of("Account with id '" + id + "' not found."));
        } catch (Exception e) {
            log.error("Error fetching last recipients for account with id {}.", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of("Failed to fetch last recipients: " + e.getMessage()));
        }
    }
}
