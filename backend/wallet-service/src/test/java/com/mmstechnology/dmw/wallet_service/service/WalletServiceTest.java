package com.mmstechnology.dmw.wallet_service.service;

import com.mmstechnology.dmw.wallet_service.model.Transaction;
import com.mmstechnology.dmw.wallet_service.model.dto.TransactionDto;
import com.mmstechnology.dmw.wallet_service.repository.WalletRepository;
import com.mmstechnology.dmw.wallet_service.service.impl.WalletServiceImpl;
import com.mmstechnology.dmw.wallet_service.util.mapper.TransactionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
}
