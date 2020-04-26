package io.swagger.configuration;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.TransactionRepository;
import io.swagger.model.Account;
import io.swagger.model.AccountBalance;
import io.swagger.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.threeten.bp.OffsetDateTime;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Component
public class AppStarter{

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @PostConstruct
    public void init(){
        initAccounts();
        initTransactions();
    }

    private void initAccounts() {
        List<Account> accounts = Arrays.asList(
                new Account(1, Account.TypeEnum.CURRENT, Account.CurrencyEnum.EUR, new AccountBalance(1, 2500.00), "NL15 INGB 0666 9476 94"),
                new Account(2, Account.TypeEnum.SAVINGS, Account.CurrencyEnum.EUR, new AccountBalance(2, 400.00), "NL15 INGB 0996 9476 94"),
                new Account(3, Account.TypeEnum.CURRENT, Account.CurrencyEnum.EUR, new AccountBalance(3, 25000.00), "NL15 INGB 0666 9346 94")
        );

        accounts.forEach(
                accountRepository::save
        );
    }

    private void initTransactions() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction(OffsetDateTime.now(), "AccountFrom1", "AccountTo1", 100.0, "Description1", 1, Transaction.TransactionTypeEnum.DEPOSIT),
                new Transaction(OffsetDateTime.now(), "AccountFrom2", "AccountTo2", 200.0, "Description2", 2, Transaction.TransactionTypeEnum.WITHDRAWAL),
                new Transaction(OffsetDateTime.now(), "AccountFrom3", "AccountTo3", 300.0, "Description3", 3, Transaction.TransactionTypeEnum.ATMDEPOSIT)
        );

        transactions.forEach(
                transactionRepository::save
        );
    }
}