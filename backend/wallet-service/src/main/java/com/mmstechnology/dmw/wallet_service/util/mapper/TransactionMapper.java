package com.mmstechnology.dmw.wallet_service.util.mapper;

import com.mmstechnology.dmw.wallet_service.model.Transaction;
import com.mmstechnology.dmw.wallet_service.model.dto.TransactionDto;

public class TransactionMapper {

    public static TransactionDto toDto(Transaction transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getDate(),
                transaction.getDescription()
        );
    }

    public static Transaction toEntity(TransactionDto transactionDto) {
        return Transaction.builder()
                .id(transactionDto.id())
                .amount(transactionDto.amount())
                .date(transactionDto.date())
                .description(transactionDto.description())
                .build();
    }
}
