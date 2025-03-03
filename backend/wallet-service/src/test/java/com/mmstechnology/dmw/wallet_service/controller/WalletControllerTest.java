package com.mmstechnology.dmw.wallet_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmstechnology.dmw.wallet_service.model.Card;
import com.mmstechnology.dmw.wallet_service.model.dto.TransactionDto;
import com.mmstechnology.dmw.wallet_service.model.dto.WalletDto;
import com.mmstechnology.dmw.wallet_service.service.ICardService;
import com.mmstechnology.dmw.wallet_service.service.IWalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IWalletService walletService;

    @MockBean
    private ICardService cardService;

    @BeforeEach
    public void setUp() {
        Mockito.when(walletService.getBalance(anyString())).thenReturn(new BigDecimal("1000.00"));
        Mockito.when(walletService.getLast5Transactions(anyString())).thenReturn(List.of(
                new TransactionDto(1L, new BigDecimal("100.00"), LocalDateTime.parse("2023-01-01T00:00:00"), "Transaction 1"),
                new TransactionDto(2L, new BigDecimal("200.00"), LocalDateTime.parse("2023-01-02T00:00:00"), "Transaction 2"),
                new TransactionDto(3L, new BigDecimal("300.00"), LocalDateTime.parse("2023-01-03T00:00:00"), "Transaction 3"),
                new TransactionDto(4L, new BigDecimal("400.00"), LocalDateTime.parse("2023-01-04T00:00:00"), "Transaction 4"),
                new TransactionDto(5L, new BigDecimal("500.00"), LocalDateTime.parse("2023-01-05T00:00:00"), "Transaction 5")
        ));
        Mockito.when(walletService.getAccountInfo(anyString())).thenReturn(new WalletDto("1", "1234567890-1234567890-1234567890-123", "alias", new BigDecimal("1000.00"), "ARS", "userId", null));

        Mockito.when(cardService.listCards(anyString())).thenReturn(List.of(
                new Card(1L, "1", "1234567890123456", "12/25", "John Doe", "Credit"),
                new Card(2L, "1", "6543210987654321", "11/24", "Jane Doe", "Debit")
        ));
        Mockito.when(cardService.getCardDetails(anyString(), anyString())).thenReturn(
                new Card(1L, "1", "1234567890123456", "12/25", "John Doe", "Credit")
        );
    }

    @Test
    public void testGetBalance() throws Exception {
        mockMvc.perform(get("/wallet/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"walletId\":\"1\",\"cvu\":\"1234567890-1234567890-1234567890-123\",\"alias\":\"alias\",\"balance\":1000.00,\"currency\":\"ARS\",\"userId\":\"userId\"}"));
    }

    @Test
    public void testGetLast5Transactions() throws Exception {
        mockMvc.perform(get("/wallet/accounts/1/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"id\":1,\"amount\":100.00,\"date\":\"2023-01-01T00:00:00\",\"description\":\"Transaction 1\"}," +
                        "{\"id\":2,\"amount\":200.00,\"date\":\"2023-01-02T00:00:00\",\"description\":\"Transaction 2\"}," +
                        "{\"id\":3,\"amount\":300.00,\"date\":\"2023-01-03T00:00:00\",\"description\":\"Transaction 3\"}," +
                        "{\"id\":4,\"amount\":400.00,\"date\":\"2023-01-04T00:00:00\",\"description\":\"Transaction 4\"}," +
                        "{\"id\":5,\"amount\":500.00,\"date\":\"2023-01-05T00:00:00\",\"description\":\"Transaction 5\"}]"));
    }

    @Test
    public void testUpdateAccount() throws Exception {
        WalletDto walletDto = new WalletDto("1", "1234567890-1234567890-1234567890-123", "newAlias", new BigDecimal("2000.00"), "ARS", "userId", null);
        ObjectMapper objectMapper = new ObjectMapper();
        String walletDtoJson = objectMapper.writeValueAsString(walletDto);

        mockMvc.perform(patch("/wallet/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(walletDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Account updated successfully."));
    }

    @Test
    public void testListCards() throws Exception {
        mockMvc.perform(get("/wallet/accounts/1/cards"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"cardId\":1,\"accountId\":\"1\",\"cardNumber\":\"1234567890123456\",\"expiryDate\":\"12/25\",\"cardHolderName\":\"John Doe\",\"cardType\":\"Credit\"}," +
                        "{\"cardId\":2,\"accountId\":\"1\",\"cardNumber\":\"6543210987654321\",\"expiryDate\":\"11/24\",\"cardHolderName\":\"Jane Doe\",\"cardType\":\"Debit\"}]"));
    }

    @Test
    public void testGetCardDetails() throws Exception {
        mockMvc.perform(get("/wallet/accounts/1/cards/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"cardId\":1,\"accountId\":\"1\",\"cardNumber\":\"1234567890123456\",\"expiryDate\":\"12/25\",\"cardHolderName\":\"John Doe\",\"cardType\":\"Credit\"}"));
    }

    @Test
    public void testAddCard() throws Exception {
        Card card = new Card(3L, "1", "1111222233334444", "10/23", "Alice Smith", "Credit");
        ObjectMapper objectMapper = new ObjectMapper();
        String cardJson = objectMapper.writeValueAsString(card);

        mockMvc.perform(post("/wallet/accounts/1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cardJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("Card added successfully."));
    }

    @Test
    public void testDeleteCard() throws Exception {
        mockMvc.perform(delete("/wallet/accounts/1/cards/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Card deleted successfully."));
    }

    @Test
    public void testRegisterMoneyIncome() throws Exception {
        TransactionDto transactionDto = new TransactionDto(1L, new BigDecimal("100.00"), LocalDateTime.parse("2023-01-01T00:00:00"), "Test transaction");
        ObjectMapper objectMapper = new ObjectMapper();
        String transactionDtoJson = objectMapper.writeValueAsString(transactionDto);

        mockMvc.perform(post("/wallet/accounts/1/transferences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionDtoJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("Money income registered successfully."));
    }

    @Test
    void testNewFunctionalitySprint2() throws Exception {
        mockMvc.perform(get("/wallet/new-functionality/test"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdatedFunctionalitySprint1() throws Exception {
        mockMvc.perform(get("/wallet/updated-functionality/test"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetLastRecipients() throws Exception {
        Mockito.when(walletService.getLastRecipients(anyString())).thenReturn(List.of("Recipient 1", "Recipient 2", "Recipient 3"));

        mockMvc.perform(get("/wallet/accounts/1/transferences"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[\"Recipient 1\", \"Recipient 2\", \"Recipient 3\"]"));
    }

    @Test
    public void testGetLastRecipientsForbidden() throws Exception {
        Mockito.when(walletService.getLastRecipients(anyString())).thenThrow(new SecurityException("Forbidden"));

        mockMvc.perform(get("/wallet/accounts/1/transferences"))
                .andExpect(status().isForbidden());
    }
}
