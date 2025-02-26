package com.mmstechnology.dmw.wallet_service.service.impl;

import com.mmstechnology.dmw.wallet_service.model.Wallet;
import com.mmstechnology.dmw.wallet_service.model.dto.WalletDto;
import com.mmstechnology.dmw.wallet_service.repository.WalletRepository;
import com.mmstechnology.dmw.wallet_service.service.IWalletService;
import com.mmstechnology.dmw.wallet_service.util.AliasGenerator;
import com.mmstechnology.dmw.wallet_service.util.CvuGenerator;
import com.mmstechnology.dmw.wallet_service.util.mapper.WalletMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
}
