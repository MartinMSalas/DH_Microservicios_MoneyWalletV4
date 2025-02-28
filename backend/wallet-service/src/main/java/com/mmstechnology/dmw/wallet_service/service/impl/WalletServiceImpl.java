package com.mmstechnology.dmw.wallet_service.service.impl;

import com.mmstechnology.dmw.wallet_service.model.Wallet;
import com.mmstechnology.dmw.wallet_service.model.Transaction;
import com.mmstechnology.dmw.wallet_service.model.dto.WalletDto;
import com.mmstechnology.dmw.wallet_service.model.dto.TransactionDto;
import com.mmstechnology.dmw.wallet_service.repository.WalletRepository;
import com.mmstechnology.dmw.wallet_service.service.IWalletService;
import com.mmstechnology.dmw.wallet_service.util.AliasGenerator;
import com.mmstechnology.dmw.wallet_service.util.CvuGenerator;
import com.mmstechnology.dmw.wallet_service.util.mapper.WalletMapper;
import com.mmstechnology.dmw.wallet_service.util.mapper.TransactionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class WalletServiceImpl implements IWalletService {

    private final WalletRepository walletRepository;

    public WalletServiceImpl(CvuGeneratorServiceImpl cvuGeneratorService, AliasGeneratorServiceImpl aliasGeneratorService, WalletRepository walletRepository) {
        CvuGenerator.setCvuGeneratorService(cvuGeneratorService);
        AliasGenerator.setAliasGeneratorService(aliasGeneratorService);
        this.walletRepository = walletRepository;
    }

    @Override
    public WalletDto createWallet(String userId) {
        log.info("Wallet service - Creating wallet for user with id: {}", userId);
        // Auto generate an UUID

        String walletId = UUID.randomUUID().toString();
        String cvu = CvuGenerator.generateCvu(walletId);
        String alias = AliasGenerator.generateAlias(walletId);

        Wallet wallet = Wallet.builder()
                .walletId(walletId)
                .balance(new BigDecimal(0))
                .currency("ARS")
                .alias(alias)
                .cvu(cvu)
                .userId(userId)
                .build();

        Wallet walletSaved = walletRepository.save(wallet);
        return WalletMapper.walletToWalletDto(walletSaved);
    }

    @Override
    public BigDecimal getBalance(String accountId) {
        Wallet wallet = walletRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        return wallet.getBalance();
    }

    @Override
    public List<TransactionDto> getLast5Transactions(String accountId) {
        Wallet wallet = walletRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        List<Transaction> transactions = walletRepository.findTop5ByAccountIdOrderByDateDesc(accountId);
        return transactions.stream().map(TransactionMapper::toDto).toList();
    }

    @Override
    public void updateAccount(String accountId, WalletDto walletDto) {
        Wallet wallet = walletRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        if (walletDto.getAlias() != null) {
            wallet.setAlias(walletDto.getAlias());
        }
        if (walletDto.getCvu() != null) {
            wallet.setCvu(walletDto.getCvu());
        }
        if (walletDto.getBalance() != null) {
            wallet.setBalance(walletDto.getBalance());
        }
        walletRepository.save(wallet);
    }

    @Override
    public List<TransactionDto> getAllActivities(String accountId) {
        Wallet wallet = walletRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        List<Transaction> transactions = walletRepository.findAllByAccountIdOrderByDateDesc(accountId);
        return transactions.stream().map(TransactionMapper::toDto).toList();
    }

    @Override
    public TransactionDto getActivityDetails(String accountId, Long transferenceID) {
        Wallet wallet = walletRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        Transaction transaction = walletRepository.findByIdAndAccountId(transferenceID, accountId).orElseThrow(() -> new RuntimeException("Transaction not found"));
        return TransactionMapper.toDto(transaction);
    }
}
