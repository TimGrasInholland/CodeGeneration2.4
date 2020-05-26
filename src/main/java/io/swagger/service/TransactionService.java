package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.dao.TransactionRepository;
import io.swagger.dao.UserRepository;
import io.swagger.model.Account;
import io.swagger.model.Transaction;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.threeten.bp.OffsetDateTime;

import java.util.ArrayList;
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
            return transactionRepository.getTransactionsByTimestampGreaterThanEqualAndTimestampIsLessThanEqual(dateFrom, dateTo, pageable);
        }
        Long id = userRepository.getUserByUsernameEquals(username).getId();
        return transactionRepository.getTransactionsByTimestampGreaterThanEqualAndTimestampIsLessThanEqualAndIdEquals(dateFrom, dateTo, id, pageable);
    }

    public List<Transaction> getTransactionsByAccountId(long accountId) {
        Account account = accountRepository.findAccountByIdAndActiveIsTrue(accountId);
        List<Transaction> transactions = (List<Transaction>) transactionRepository.getTransactionsByAccountFromEqualsOrAccountToEqualsOrderByTimestampDesc(account.getIban(), account.getIban());
        return transactions;
    }

    public List<Transaction> getTransactionsByIban(String iban) {
        return (List<Transaction>) transactionRepository.getTransactionsByIban(iban);
    }

    public List<Transaction> getTransactionsByUserId(Long id) {
        //Get all accounts of user from userId
        List<Account> userAccounts = (List<Account>) accountRepository.findAccountsByUserIdAndActiveIsTrue(id);
        List<Transaction> transactions = new ArrayList<>();

        //Get foreach account all send and received transactions
        userAccounts.forEach(account ->
                transactions.addAll(getTransactionsByIban(account.getIban()))
        );

        return transactions;
    }

    public Integer getDailyTransactionsByUserPerforming(Long userPerformingId, OffsetDateTime minDate, OffsetDateTime maxDate) {
        return transactionRepository.countTransactionsByUserPerformingIdEqualsAndTimestampBetween(userPerformingId, minDate, maxDate);
    }

    @Modifying
    public void updateAccount(Account account) {
        accountRepository.save(account);
    }

    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public Integer countAllTransactions(){ return transactionRepository.countAllTransactions();}
}
