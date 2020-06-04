package io.swagger.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

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
                () -> transaction.setAmount(-10.0));
        assertEquals("Invalid amount.", exception.getMessage());
    }
}
