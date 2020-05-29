package io.swagger.controller;

import io.swagger.model.Account;
import io.swagger.model.AccountBalance;
import io.swagger.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AccountsControllerTest {

    @Autowired
    private AccountService service;
    private Account account;

    @BeforeEach
    public void setup() {
        account = new Account(2L, Account.TypeEnum.CURRENT, Account.CurrencyEnum.EUR, new AccountBalance(2L, 500.00), "NL01INHO8374054831", true);
    }

    @Test
    public void createAnAccountIsEqualsToSetup(){
        service.createAccount(account);
        assertEquals(service.getAccountByIBAN(account.getIban()), account);
    }

    @Test
    public void givenAccountShouldAlreadyBeenUsed(){
        boolean accountAlreadyExists = true;
        if(service.countAccountByIBAN(account.getIban()) == 0){
            accountAlreadyExists = false;
        }
        assertEquals(accountAlreadyExists, true);
    }

    @Test
    public void limitShouldReturnThreeItems(){
        int limit = 3;
        Pageable pageable = PageRequest.of(1, limit);
        List<Account> ls = service.getAllAccountsWithParams(pageable, "%");
        assertEquals(ls.size(), limit);
    }

    @Test
    public void disabledAccountShouldBeInactive(){
        account.setActive(false);
        service.disableAccount(account);
        assertEquals(service.getAccountById(7L).isActive(),false);
    }
}
