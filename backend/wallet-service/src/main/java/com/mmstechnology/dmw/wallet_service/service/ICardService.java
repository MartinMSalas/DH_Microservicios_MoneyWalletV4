package com.mmstechnology.dmw.wallet_service.service;

import com.mmstechnology.dmw.wallet_service.model.Card;

import java.util.List;

public interface ICardService {
    List<Card> listCards(String accountId);
    Card getCardDetails(String accountId, String cardId);
    void addCard(String accountId, Card card);
    void deleteCard(String accountId, String cardId);
}
