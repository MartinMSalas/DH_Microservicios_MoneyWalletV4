package com.mmstechnology.dmw.wallet_service.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record WalletDto(String walletId, String cvu, String alias, BigDecimal balance, String currency, String userId) {
}
