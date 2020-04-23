package io.swagger.configuration;

import io.swagger.dao.AccountRepository;
import io.swagger.model.Account;
import io.swagger.model.Balance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AppStarter implements ApplicationRunner {

    private AccountRepository accountRepository;


    public AppStarter(){}

    public AppStarter(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        List<Account> accounts = Arrays.asList(
                new Account(1, Account.TypeEnum.CURRENT, Account.CurrencyEnum.EUR, new Balance(1, 300.00), "NL15 INGB 0666 9476 94")
        );

        accounts.forEach(
                accountRepository::save
        );

        accountRepository.findAll().forEach(System.out::println);
    }
}
