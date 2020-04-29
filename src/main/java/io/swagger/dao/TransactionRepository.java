package io.swagger.dao;

import io.swagger.model.Account;
import io.swagger.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction as t, Account a WHERE a.id = ?1")
    Iterable<Transaction> getTransactionsByAccountId(long id);

    // find all accounts with query values
    Page<Transaction> findAll(Pageable pageable);

    @Query("SELECT t FROM Transaction as t WHERE t.timestamp BETWEEN :dateFrom AND :dateTo")
    Iterable<Transaction> findTransactionsByTimestampBetween(OffsetDateTime dateFrom, OffsetDateTime dateTo);
}
