package io.swagger.configuration;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.SessionTokenRepository;
import io.swagger.dao.TransactionRepository;
import io.swagger.dao.UserRepository;
import io.swagger.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.threeten.bp.LocalDate;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "bankapi.autorun", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AppStarter{

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionTokenRepository sessionTokenRepository;

    @PostConstruct
    public void init(){
        initAccounts();
        initTransactions();
        initUsers();
        initTestSessionToken();
    }

    private void initTestSessionToken(){
        List<SessionToken> sessionTokens = Arrays.asList(
                new SessionToken("0", 3L, User.TypeEnum.CUSTOMER),
                new SessionToken("1", 1L, User.TypeEnum.BANK),
                new SessionToken("testEmployee", 2L, User.TypeEnum.EMPLOYEE)
        );
        sessionTokens.forEach(
                sessionTokenRepository::save
        );
    }

    private void initAccounts() {
        List<Account> accounts = Arrays.asList(
                new Account(1L, Account.TypeEnum.CURRENT, Account.CurrencyEnum.EUR, null, "NL01INHO0000000001", true),
                new Account(3L, Account.TypeEnum.SAVINGS, Account.CurrencyEnum.EUR, null, "NL01INHO4996947694", true),
                new Account(3L, Account.TypeEnum.SAVINGS, Account.CurrencyEnum.EUR, null, "NL01INHO4995677694", true),
                new Account(2L, Account.TypeEnum.CURRENT, Account.CurrencyEnum.EUR, null, "NL01INHO6666934694", true),
                new Account(2L, Account.TypeEnum.CURRENT, Account.CurrencyEnum.EUR, null, "NL01INHO6666134694", true)
        );

        accounts.forEach(
                accountRepository::save
        );
        accounts.forEach(
                account ->
                        account.setBalance(new AccountBalance(account.getId(), 150.00))
        );

        //TODO: ff netter maken
        accounts.forEach(
                accountRepository::save
        );
    }

    private void initTransactions() {
        List<Transaction> transactions = Arrays.asList(
                // illegal transactions
                new Transaction("NL01INHO6666934694", "NL01INHO4996947694", 100.0, "Description1", 2L, Transaction.TransactionTypeEnum.DEPOSIT),
                new Transaction("NL01INHO4996947694", "NL01INHO6666934694", 50.0, "Description2", 2L, Transaction.TransactionTypeEnum.WITHDRAWAL),
                new Transaction("NL01INHO6666134694", "NL01INHO6666934694", 10.0, "Description3", 3L, Transaction.TransactionTypeEnum.PAYMENT)
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