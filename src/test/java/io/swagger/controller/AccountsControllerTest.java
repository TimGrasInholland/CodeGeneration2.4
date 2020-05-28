package io.swagger.controller;

import io.swagger.model.Account;
import io.swagger.model.AccountBalance;
import io.swagger.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.BDDMockito.given;

@SpringBootTest
class AccountsControllerTest {

    @Autowired
    @MockBean
    private AccountService service;
    private Account account;

    @BeforeEach
    public void setup() {
        account = new Account(2L, Account.TypeEnum.CURRENT, Account.CurrencyEnum.EUR, new AccountBalance(2L, 500.00), "NL01INHO8374054831", true);
    }

    @Test
    public void getAllAccountsShouldReturnJsonArray() {
        given(service.getAllAccounts()).willReturn(Arrays.asList(account));
    }

    @Test
    public void getAccountsByUserIdShouldReturnAccounts(){
        given(service.getAccountsByUserId(2L)).willReturn(Arrays.asList(account));
    }

    @Test
    public void createAnAccountIsEqualsToSetup(){
        service.createAccount(account);
        given(service.getAccountByIBAN("NL01INHO8374054831")).willReturn(account);
    }

    @Test
    public void disabledAccountShouldBeInactive(){
        service.disableAccount(account);
        assertEquals(service.getAccountByIBAN("NL01INHO8374054831"),false);
    }
}
