package io.swagger.configuration;

import io.swagger.dao.AccountRepository;
import io.swagger.model.Account;
import io.swagger.model.AccountBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Component
public class AppStarter{

    @Autowired
    private AccountRepository accountRepository;

    @PostConstruct
    public void init(){
        List<Account> accounts = Arrays.asList(
                new Account(1, Account.TypeEnum.CURRENT, Account.CurrencyEnum.EUR, new AccountBalance(1, 2500.00), "NL15 INGB 0666 9476 94"),
                new Account(2, Account.TypeEnum.SAVINGS, Account.CurrencyEnum.EUR, new AccountBalance(2, 400.00), "NL15 INGB 0996 9476 94"),
                new Account(3, Account.TypeEnum.CURRENT, Account.CurrencyEnum.EUR, new AccountBalance(3, 25000.00), "NL15 INGB 0666 9346 94")
        );

        accounts.forEach(
                accountRepository::save
        );

        accountRepository.findAll().forEach(System.out::println);
    }
}