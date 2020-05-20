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
    public void createGuitarShouldNotBeNull() {
        assertNotNull(account);
    }
}
