package io.swagger.dao;

import io.swagger.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    @Query("SELECT id, timestamp, accountFrom, accountTo, amount, userPerforming, transactionType FROM Transaction WHERE id=?1")
    Iterable<Transaction> getTransactionsByIdEquals(int accountId);
}
