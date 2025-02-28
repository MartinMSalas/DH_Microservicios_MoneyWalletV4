package com.mmstechnology.dmw.wallet_service.controller;

import com.mmstechnology.dmw.wallet_service.model.dto.TransactionDto;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IWalletService walletService;

    @BeforeEach
    public void setUp() {
        Mockito.when(walletService.getBalance(anyString())).thenReturn(new BigDecimal("1000.00"));
        Mockito.when(walletService.getLast5Transactions(anyString())).thenReturn(List.of(
                new TransactionDto("1", new BigDecimal("100.00"), "2023-01-01", "Transaction 1"),
                new TransactionDto("2", new BigDecimal("200.00"), "2023-01-02", "Transaction 2"),
                new TransactionDto("3", new BigDecimal("300.00"), "2023-01-03", "Transaction 3"),
                new TransactionDto("4", new BigDecimal("400.00"), "2023-01-04", "Transaction 4"),
                new TransactionDto("5", new BigDecimal("500.00"), "2023-01-05", "Transaction 5")
        ));
    }

    @Test
    public void testGetBalance() throws Exception {
        mockMvc.perform(get("/wallet/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("1000.00"));
    }

    @Test
    public void testGetLast5Transactions() throws Exception {
        mockMvc.perform(get("/wallet/accounts/1/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"id\":\"1\",\"amount\":100.00,\"date\":\"2023-01-01\",\"description\":\"Transaction 1\"}," +
                        "{\"id\":\"2\",\"amount\":200.00,\"date\":\"2023-01-02\",\"description\":\"Transaction 2\"}," +
                        "{\"id\":\"3\",\"amount\":300.00,\"date\":\"2023-01-03\",\"description\":\"Transaction 3\"}," +
                        "{\"id\":\"4\",\"amount\":400.00,\"date\":\"2023-01-04\",\"description\":\"Transaction 4\"}," +
                        "{\"id\":\"5\",\"amount\":500.00,\"date\":\"2023-01-05\",\"description\":\"Transaction 5\"}]"));
    }
}
