package io.swagger.controller;

import io.swagger.model.Account;
import io.swagger.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;

@SpringBootTest
class AccountsControllerTest {
    @Autowired
    @MockBean
    private AccountService service;
    private Account account;

    @BeforeEach
    public void setup() {
        account = new Account(2L, Account.TypeEnum.CURRENT, Account.CurrencyEnum.EUR, null, "NL01INHO8374054831", true);
    }

    @Test
    public void getAllAccountsShouldReturnJsonArray() throws Exception {
        given(service.getAllAccounts()).willReturn(Arrays.asList(account));
    }


}
