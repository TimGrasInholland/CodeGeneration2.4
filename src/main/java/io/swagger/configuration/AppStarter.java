package io.swagger.configuration;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.TransactionRepository;
import io.swagger.dao.UserRepository;
import io.swagger.model.Account;
import io.swagger.model.AccountBalance;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.threeten.bp.LocalDate;
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

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init(){
        initAccounts();
        initTransactions();
        initUsers();
    }

    private void initAccounts() {
        List<Account> accounts = Arrays.asList(
                new Account(1L, Account.TypeEnum.CURRENT, Account.CurrencyEnum.EUR, null, "NL01 INHO 0000 0000 01", true),
                new Account(2L, Account.TypeEnum.SAVINGS, Account.CurrencyEnum.EUR, null, "NL43 INHO 0996 9476 94", true),
                new Account(3L, Account.TypeEnum.CURRENT, Account.CurrencyEnum.EUR, null, "NL15 INHO 0666 9346 94", true)
        );

        accounts.forEach(
                accountRepository::save
        );
        accounts.forEach(
                account ->
                        account.setBalance(new AccountBalance(account.getId(), 0.00))
        );

        //TODO: ff netter maken
        accounts.forEach(
                accountRepository::save
        );
    }

    private void initTransactions() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction(OffsetDateTime.now(), "AccountFrom1", "AccountTo1", 100.0, "Description1", 1L, Transaction.TransactionTypeEnum.DEPOSIT),
                new Transaction(OffsetDateTime.now(), "AccountFrom2", "AccountTo2", 200.0, "Description2", 2L, Transaction.TransactionTypeEnum.WITHDRAWAL),
                new Transaction(OffsetDateTime.now(), "AccountFrom3", "AccountTo3", 300.0, "Description3", 3L, Transaction.TransactionTypeEnum.DEPOSIT)
        );

        transactions.forEach(
                transactionRepository::save
        );
    }

    private void initUsers() {
        List<User> users = Arrays.asList(
                new User("Inholland-Bank", "Welcome567?", "Bank", "", "Inholland", "bank@inholland-bank.nl",
                        LocalDate.of(2019, 1, 1), "Arnold straat 33", "1354PK", "Haarlem", "0638313905", User.TypeEnum.BANK, true),
                new User("Adrie538", "Welkom123!", "Andries", "", "Komen", "AndriesK@gmail.com",
                        LocalDate.of(1992, 11, 3), "Bloemendotter 12", "1958TX", "Haarlem", "0637291827", User.TypeEnum.EMPLOYEE, true),
                new User("SjaakMaster", "Test123!", "Sjaak", "Van", "Bergen", "SjaakVBergen@gmail.com",
                        LocalDate.of(2000, 12, 12), "Jacobstraat", "1938DR", "Amsterdam", "0638273745", User.TypeEnum.CUSTOMER, true)
        );

        users.forEach(
                userRepository::save
        );
    }
}