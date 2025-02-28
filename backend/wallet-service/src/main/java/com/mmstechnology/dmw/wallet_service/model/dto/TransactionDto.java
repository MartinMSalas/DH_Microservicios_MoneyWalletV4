package com.mmstechnology.dmw.wallet_service.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDto(Long id, BigDecimal amount, LocalDateTime date, String description) {
}
