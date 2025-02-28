package com.mmstechnology.dmw.wallet_service.service;

import com.mmstechnology.dmw.wallet_service.model.Card;
import com.mmstechnology.dmw.wallet_service.repository.CardRepository;
import com.mmstechnology.dmw.wallet_service.service.impl.CardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

public class CardServiceTest {

    private CardRepository cardRepository;
    private ICardService cardService;

    @BeforeEach
    public void setUp() {
        cardRepository = Mockito.mock(CardRepository.class);
        cardService = new CardServiceImpl(cardRepository);
    }

    @Test
    public void testListCards() {
        Mockito.when(cardRepository.findByAccountId(anyString())).thenReturn(List.of(
                new Card(1L, "1", "1234567890123456", "12/25", "John Doe", "Credit"),
                new Card(2L, "1", "6543210987654321", "11/24", "Jane Doe", "Debit")
        ));

        List<Card> cards = cardService.listCards("1");
        assertEquals(2, cards.size());
    }

    @Test
    public void testGetCardDetails() {
        Mockito.when(cardRepository.findByIdAndAccountId(anyString(), anyString())).thenReturn(Optional.of(
                new Card(1L, "1", "1234567890123456", "12/25", "John Doe", "Credit")
        ));

        Card card = cardService.getCardDetails("1", "1");
        assertNotNull(card);
        assertEquals("1234567890123456", card.getCardNumber());
    }

    @Test
    public void testAddCard() {
        Card card = new Card(3L, "1", "1111222233334444", "10/23", "Alice Smith", "Credit");

        Mockito.when(cardRepository.existsByCardNumber(anyString())).thenReturn(false);
        Mockito.when(cardRepository.save(card)).thenReturn(card);

        assertDoesNotThrow(() -> cardService.addCard("1", card));
    }

    @Test
    public void testDeleteCard() {
        Mockito.when(cardRepository.existsByIdAndAccountId(anyString(), anyString())).thenReturn(true);
        Mockito.doNothing().when(cardRepository).deleteByIdAndAccountId(anyString(), anyString());

        assertDoesNotThrow(() -> cardService.deleteCard("1", "1"));
    }

    @Test
    public void testNewFunctionalitySprint2() {
        // Add test case for new functionality in Sprint 2
        Card card = new Card(4L, "2", "2222333344445555", "09/24", "Bob Johnson", "Debit");
        Mockito.when(cardRepository.save(card)).thenReturn(card);
        assertDoesNotThrow(() -> cardService.addCard("2", card));
    }

    @Test
    public void testUpdatedFunctionalitySprint1() {
        // Update test case for functionality from Sprint 1
        Mockito.when(cardRepository.findByIdAndAccountId(anyString(), anyString())).thenReturn(Optional.of(
                new Card(1L, "1", "1234567890123456", "12/25", "John Doe", "Credit")
        ));
        Card card = cardService.getCardDetails("1", "1");
        assertNotNull(card);
        assertEquals("1234567890123456", card.getCardNumber());
    }
}
