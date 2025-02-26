package com.mmstechnology.dmw.wallet_service.service;

import com.mmstechnology.dmw.wallet_service.model.dto.WalletDto;

public interface IWalletService {

    WalletDto createWallet(String userId);
}
