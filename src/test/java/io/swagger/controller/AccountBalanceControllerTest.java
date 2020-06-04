package io.swagger.controller;

import io.swagger.model.Account;
import io.swagger.model.AccountBalance;
import io.swagger.service.AccountBalanceService;
import io.swagger.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AccountBalanceControllerTest {
    @Autowired
    private AccountBalanceService accountBalanceService;
    @Autowired
    private AccountService accountService;
    private Account account;
    private AccountBalance accountBalance;
    private Long id;

    @BeforeEach
    public void setup() {
        id = 7L;
        accountBalance = new AccountBalance(id, 140.00);
        account = new Account(2L, Account.TypeEnum.CURRENT, Account.CurrencyEnum.EUR, accountBalance, "NL01INHO8374054831", true);
    }

    @Test
    public void getAccountBalanceByAccountId() {
        accountService.createAccount(account);
        assertEquals(accountBalanceService.getAccountBalance(id), accountBalance);
    }

    @Test
    public void updateAccountBalance() {
        //Update account balance and check if result is equal to given account balance
        AccountBalance putAccountBalance = accountBalanceService.getAccountBalance(id);
        putAccountBalance.setBalance(160.00);
        accountBalanceService.updateAccountBalance(putAccountBalance);
        assertEquals(accountBalanceService.getAccountBalance(id), putAccountBalance);
    }

}
