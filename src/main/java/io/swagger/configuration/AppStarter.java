package io.swagger.configuration;

import io.swagger.dao.AccountRepository;
import io.swagger.model.Account;
import io.swagger.model.Balance;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Arrays;
import java.util.List;

public class AppStarter implements ApplicationRunner {
    AccountRepository accountRepository;

    public AppStarter(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        List<Account> accounts = Arrays.asList(
                new Account(1, Account.TypeEnum.CURRENT, Account.CurrencyEnum.EUR, new Balance(1, 300.00), "LKDJFLSDJ")
        );

        accounts.forEach(
                accountRepository::save
        );

        accountRepository.findAll().forEach(System.out::println);
    }
}
