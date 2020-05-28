package io.swagger.model;

import io.swagger.api.TransactionsApiController;
import io.swagger.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionTest {
    @Autowired
    @MockBean
    private TransactionService service;

    @Autowired
    private TransactionsApiController controller;

    private Transaction transaction;

    @BeforeEach
    public void Setup() {
        transaction = new Transaction();
    }

    @Test
    public void createTransactionShouldNotBeNull() {
        assertNotNull(transaction);
    }

    @Test
    public void setNegativeAmountShouldThrowError() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                ()-> transaction.setAmount(-10.0));
        assertEquals("Amount cannot be below zero.", exception.getMessage());
    }
}
