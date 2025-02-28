package com.mmstechnology.dmw.wallet_service.service.impl;

import com.mmstechnology.dmw.wallet_service.model.Card;
import com.mmstechnology.dmw.wallet_service.repository.CardRepository;
import com.mmstechnology.dmw.wallet_service.service.ICardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements ICardService {

    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public List<Card> listCards(String accountId) {
        return cardRepository.findByAccountId(accountId);
    }

    @Override
    public Card getCardDetails(String accountId, String cardId) {
        return cardRepository.findByIdAndAccountId(cardId, accountId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
    }

    @Override
    public void addCard(String accountId, Card card) {
        if (cardRepository.existsByCardNumber(card.getCardNumber())) {
            throw new CardAlreadyExistsException("Card already exists in another account");
        }
        card.setAccountId(accountId);
        cardRepository.save(card);
    }

    @Override
    public void deleteCard(String accountId, String cardId) {
        if (!cardRepository.existsByIdAndAccountId(cardId, accountId)) {
            throw new CardNotFoundException("Card not found");
        }
        cardRepository.deleteByIdAndAccountId(cardId, accountId);
    }
}
