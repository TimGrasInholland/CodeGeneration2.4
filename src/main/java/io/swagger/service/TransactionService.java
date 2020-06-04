package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.TransactionRepository;
import io.swagger.dao.UserRepository;
import io.swagger.model.Account;
import io.swagger.model.Transaction;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.threeten.bp.OffsetDateTime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public List<Transaction> getAllTransactions(OffsetDateTime dateFrom, OffsetDateTime dateTo, Integer offset, Integer limit, String username) {
        Pageable pageable = PageRequest.of(offset, limit);
        if (username.equals("%")) {
            return transactionRepository.getTransactionsByTimestampGreaterThanEqualAndTimestampIsLessThanEqualOrderByTimestampDesc(dateFrom, dateTo, pageable);
        }

        // Get userId by username in order to grab relevant accounts
        Long id = userRepository.getUserByUsernameEqualsAndActiveIsTrue(username).getId();

        // get all accounts with userId x
        List<Account> accounts = (List<Account>) accountRepository.findAccountsByUserId(id);
        // get all transactions with ibanFrom x or ibanTo x
        List<Transaction> allTransactions = new ArrayList<>();
        for (Account account : accounts) {
            allTransactions.addAll(transactionRepository.getTransactionsByTimestampGreaterThanEqualAndTimestampIsLessThanEqualAndAccountFromEqualsOrAccountToEqualsOrderByTimestampDesc(dateFrom, dateTo, account.getIban(), account.getIban(), pageable));
        }
        // remove duplicates and return
        return new ArrayList<>(new HashSet<>(allTransactions));
    }

    public List<Transaction> getTransactionsByAccountId(long accountId) {
        Account account = accountRepository.getAccountById(accountId);
        return (List<Transaction>) transactionRepository.getTransactionsByAccountFromEqualsOrAccountToEqualsOrderByTimestampDesc(account.getIban(), account.getIban());
    }

    public List<Transaction> getTransactionsByIban(String iban) {
        return (List<Transaction>) transactionRepository.getTransactionsByIban(iban);
    }

    public List<Transaction> getTransactionsByUserId(Long id) {
        //Get all accounts of user from userId
        List<Account> userAccounts = (List<Account>) accountRepository.findAccountsByUserId(id);
        List<Transaction> transactions = new ArrayList<>();

        //Get foreach account all send and received transactions
        userAccounts.forEach(account ->
                transactions.addAll(getTransactionsByIban(account.getIban()))
        );

        return transactions;
    }

    public Integer getDailyTransactionsByUserPerforming(Long userPerformingId, OffsetDateTime minDate, OffsetDateTime maxDate) {
        return transactionRepository.countTransactionsByUserPerformingIdEqualsAndTimestampBetweenOrderByTimestampDesc(userPerformingId, minDate, maxDate);
    }

    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public Integer countAllTransactions() {
        return transactionRepository.countAllTransactions();
    }

}
