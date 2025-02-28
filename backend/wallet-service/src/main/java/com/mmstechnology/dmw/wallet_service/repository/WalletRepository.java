package com.mmstechnology.dmw.wallet_service.repository;

import com.mmstechnology.dmw.wallet_service.model.Wallet;
import com.mmstechnology.dmw.wallet_service.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface WalletRepository extends CrudRepository<Wallet, String> {
    BigDecimal findBalanceByAccountId(String accountId);
    List<Transaction> findTop5ByAccountIdOrderByDateDesc(String accountId);
}
