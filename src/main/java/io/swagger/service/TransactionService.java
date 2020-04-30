package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.TransactionRepository;
import io.swagger.model.Account;
import io.swagger.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.threeten.bp.OffsetDateTime;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public List<Transaction> getAllTransactions() {
        return (List<Transaction>) transactionRepository.findAll();
    }

    public List<Transaction> getAllTransactionsWithQuery(int offset, int limit) {
        Pageable pageable = new PageRequest(offset,limit);
        Page<Transaction> page = transactionRepository.findAll(pageable);
        return page.getContent();
    }

    public List<Transaction> getAllTransactionsByAccountId(long id) {
        return (List<Transaction>) transactionRepository.getTransactionsByAccountId(id);
    }

    public List<Transaction> getTransactionFilterDate(OffsetDateTime dateFrom, OffsetDateTime dateTo) {
        return (List<Transaction>) transactionRepository.getTransactionsByTimestampGreaterThanEqualAndTimestampIsLessThanEqual(dateFrom, dateTo);
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
}
