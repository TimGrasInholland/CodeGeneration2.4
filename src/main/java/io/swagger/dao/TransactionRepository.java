package io.swagger.dao;

import io.swagger.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.threeten.bp.OffsetDateTime;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction as t, Account a WHERE a.id = ?1")
    Iterable<Transaction> getTransactionsByAccountId(int accountId);

    Iterable<Transaction> getTransactionsByTimestampGreaterThanEqualAndTimestampIsLessThanEqual(OffsetDateTime ld1, OffsetDateTime ld2);

}
