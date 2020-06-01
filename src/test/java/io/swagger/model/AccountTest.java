package io.swagger.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    private Account account;

    @BeforeEach
    public void Setup() {
        account = new Account();
    }

    @Test
    public void createAccountShouldNotBeNull() {
        assertNotNull(account);
    }

    @Test
    public void ibanShouldHaveAnRequiredPattern(){
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> account.setIban("01NLINGB0384739473944"));
        assertEquals("Invalid iban", exception.getMessage());
    }
}
