package com.mmstechnology.dmw.wallet_service.util.mapper;

import com.mmstechnology.dmw.wallet_service.model.Wallet;
import com.mmstechnology.dmw.wallet_service.model.dto.WalletDto;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

    public static WalletDto walletToWalletDto(Wallet wallet) {

        return new WalletDto(wallet.getWalletId(), wallet.getCvu(), wallet.getAlias(), wallet.getBalance(), wallet.getCurrency(), wallet.getUserId());
    }

    public static Wallet walletDtoToWallet(WalletDto walletDto) {
        return Wallet.builder()
                .walletId(walletDto.walletId())
                .cvu(walletDto.cvu())
                .alias(walletDto.alias())
                .balance(walletDto.balance())
                .currency(walletDto.currency())
                .userId(walletDto.userId())
                .build();
    }
}
