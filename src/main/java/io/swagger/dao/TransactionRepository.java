package io.swagger.dao;

import io.swagger.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.threeten.bp.OffsetDateTime;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {
    
    @Query("SELECT t FROM Transaction as t, Account a WHERE a.id = ?1")
    Iterable<Transaction> getTransactionsByAccountId(long accountId);

    @Query("SELECT t FROM Transaction AS t WHERE t.accountFrom LIKE ?1 OR t.accountTo LIKE ?1 ORDER BY t.timestamp")
    Iterable<Transaction> getTransactionsByIban(String iban);

    Iterable<Transaction> getTransactionsByTimestampGreaterThanEqualAndTimestampIsLessThanEqual(OffsetDateTime dateFrom, OffsetDateTime dateTo);

}
