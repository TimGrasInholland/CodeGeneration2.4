package io.swagger.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountBalanceTest {

    private AccountBalance accountBalance;

    @BeforeEach
    public void Setup() {
        accountBalance = new AccountBalance();
    }

    @Test
    public void createAccountBalanceShouldNotBeNull() {
        assertNotNull(accountBalance);
    }

    @Test
    public void balanceAmountCanNotBeLowerThenBankConfigLimit(){
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> accountBalance.setBalance(-100.00));
        assertEquals("Limit of account balance has reached!", exception.getMessage());
    }

    @Test
    public void checkIfBanlanceValueHasTwoDecimals(){
        Exception exception0 = assertThrows(IllegalArgumentException.class,
                () -> accountBalance.setBalance(8.000));
        assertEquals("Balance value has not the right format. Must have 2 decimals", exception0.getMessage());

        Exception exception1 = assertThrows(IllegalArgumentException.class,
                () -> accountBalance.setBalance(8.0));
        assertEquals("Balance value has not the right format. Must have 2 decimals", exception1.getMessage());
    }
}
