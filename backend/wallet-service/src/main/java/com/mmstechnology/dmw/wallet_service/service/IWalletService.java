package com.mmstechnology.dmw.wallet_service.service;

import com.mmstechnology.dmw.wallet_service.model.dto.WalletDto;
import com.mmstechnology.dmw.wallet_service.model.dto.TransactionDto;

import java.math.BigDecimal;
import java.util.List;

public interface IWalletService {

    WalletDto createWallet(String userId);

    BigDecimal getBalance(String accountId);

    List<TransactionDto> getLast5Transactions(String accountId);

    void updateAccount(String accountId, WalletDto walletDto);

    List<TransactionDto> getAllActivities(String accountId);

    TransactionDto getActivityDetails(String accountId, Long transferenceID);

    void registerMoneyIncome(String accountId, TransactionDto transactionDto);

    List<String> getLastRecipients(String accountId);
}
