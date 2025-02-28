package com.mmstechnology.dmw.wallet_service.repository;

import com.mmstechnology.dmw.wallet_service.model.Card;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends CrudRepository<Card, String> {
    List<Card> findByAccountId(String accountId);
    Optional<Card> findByIdAndAccountId(String cardId, String accountId);
    boolean existsByCardNumber(String cardNumber);
    boolean existsByIdAndAccountId(String cardId, String accountId);
    void deleteByIdAndAccountId(String cardId, String accountId);
}
