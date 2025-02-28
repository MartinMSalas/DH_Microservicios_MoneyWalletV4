package com.mmstechnology.dmw.wallet_service.service;

import com.mmstechnology.dmw.wallet_service.model.Transaction;
import com.mmstechnology.dmw.wallet_service.model.Wallet;
import com.mmstechnology.dmw.wallet_service.model.dto.TransactionDto;
import com.mmstechnology.dmw.wallet_service.model.dto.WalletDto;
import com.mmstechnology.dmw.wallet_service.repository.WalletRepository;
import com.mmstechnology.dmw.wallet_service.service.impl.WalletServiceImpl;
import com.mmstechnology.dmw.wallet_service.util.mapper.TransactionMapper;
import com.mmstechnology.dmw.wallet_service.util.mapper.WalletMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBalance() {
        String accountId = "test-account-id";
        BigDecimal expectedBalance = new BigDecimal("100.00");

        when(walletRepository.findById(accountId)).thenReturn(Optional.of(new Wallet(accountId, "test-user-id", "test-cvu", "test-alias", expectedBalance, "ARS", null, null, null)));

        BigDecimal actualBalance = walletService.getBalance(accountId);

        assertEquals(expectedBalance, actualBalance);
        verify(walletRepository, times(1)).findById(accountId);
    }

    @Test
    public void testGetLast5Transactions() {
        String accountId = "test-account-id";
        List<Transaction> transactions = List.of(
                new Transaction(1L, accountId, new BigDecimal("10.00"), LocalDateTime.now(), "Transaction 1"),
                new Transaction(2L, accountId, new BigDecimal("20.00"), LocalDateTime.now(), "Transaction 2"),
                new Transaction(3L, accountId, new BigDecimal("30.00"), LocalDateTime.now(), "Transaction 3"),
                new Transaction(4L, accountId, new BigDecimal("40.00"), LocalDateTime.now(), "Transaction 4"),
                new Transaction(5L, accountId, new BigDecimal("50.00"), LocalDateTime.now(), "Transaction 5")
        );

        when(walletRepository.findTop5ByAccountIdOrderByDateDesc(accountId)).thenReturn(transactions);

        List<TransactionDto> actualTransactions = walletService.getLast5Transactions(accountId);

        assertEquals(transactions.size(), actualTransactions.size());
        for (int i = 0; i < transactions.size(); i++) {
            assertEquals(TransactionMapper.toDto(transactions.get(i)), actualTransactions.get(i));
        }
        verify(walletRepository, times(1)).findTop5ByAccountIdOrderByDateDesc(accountId);
    }

    @Test
    public void testCreateWallet() {
        String userId = "test-user-id";
        String walletId = UUID.randomUUID().toString();
        String cvu = "test-cvu";
        String alias = "test-alias";
        Wallet wallet = new Wallet(walletId, userId, cvu, alias, BigDecimal.ZERO, "ARS", null, null, null);

        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        WalletDto walletDto = walletService.createWallet(userId);

        assertEquals(walletId, walletDto.walletId());
        assertEquals(userId, walletDto.userId());
        assertEquals(cvu, walletDto.cvu());
        assertEquals(alias, walletDto.alias());
        assertEquals(BigDecimal.ZERO, walletDto.balance());
        assertEquals("ARS", walletDto.currency());
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    public void testUpdateAccount() {
        String accountId = "test-account-id";
        WalletDto walletDto = new WalletDto(accountId, "new-cvu", "new-alias", new BigDecimal("200.00"), "ARS", "test-user-id", null);
        Wallet wallet = new Wallet(accountId, "test-user-id", "test-cvu", "test-alias", new BigDecimal("100.00"), "ARS", null, null, null);

        when(walletRepository.findById(accountId)).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        walletService.updateAccount(accountId, walletDto);

        assertEquals(walletDto.cvu(), wallet.getCvu());
        assertEquals(walletDto.alias(), wallet.getAlias());
        assertEquals(walletDto.balance(), wallet.getBalance());
        verify(walletRepository, times(1)).findById(accountId);
        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    public void testNewFunctionalitySprint2() {
        // Add test case for new functionality in Sprint 2
        String userId = "new-user-id";
        WalletDto walletDto = walletService.createWallet(userId);
        assertEquals(userId, walletDto.userId());
    }

    @Test
    public void testUpdatedFunctionalitySprint1() {
        // Update test case for functionality from Sprint 1
        String accountId = "test-account-id";
        BigDecimal expectedBalance = new BigDecimal("150.00");

        when(walletRepository.findById(accountId)).thenReturn(Optional.of(new Wallet(accountId, "test-user-id", "test-cvu", "test-alias", expectedBalance, "ARS", null, null, null)));

        BigDecimal actualBalance = walletService.getBalance(accountId);

        assertEquals(expectedBalance, actualBalance);
        verify(walletRepository, times(1)).findById(accountId);
    }
}
