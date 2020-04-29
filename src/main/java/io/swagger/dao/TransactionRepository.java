package io.swagger.dao;

import io.swagger.model.Account;
import io.swagger.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    @Query("SELECT timestamp, accountFrom, accountTo, amount, description, userPerformingId FROM Transaction WHERE Transaction.userPerformingId = " + )
    public Iterable<Transaction> findTransactionsByUserId(Long id);

}
