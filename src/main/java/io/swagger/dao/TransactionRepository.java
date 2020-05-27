package io.swagger.dao;

import io.swagger.model.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.threeten.bp.OffsetDateTime;

import java.util.List;

@Repository
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {

    Iterable<Transaction> getTransactionsByAccountFromEqualsOrAccountToEqualsOrderByTimestampDesc(String accountFrom, String accountTo);

    @Query("SELECT t FROM Transaction AS t WHERE t.accountFrom LIKE ?1 OR t.accountTo LIKE ?1 ORDER BY t.timestamp ASC")
    Iterable<Transaction> getTransactionsByIban(String iban);

    List<Transaction> getTransactionsByTimestampGreaterThanEqualAndTimestampIsLessThanEqualOrderByTimestampDesc(OffsetDateTime dateFrom, OffsetDateTime dateTo, Pageable pageable);

    Integer countTransactionsByUserPerformingIdEqualsAndTimestampBetweenOrderByTimestampDesc(Long userPerformingId, OffsetDateTime minDate, OffsetDateTime maxDate);

    @Query("SELECT COUNT(t) FROM Transaction as t")
    Integer countAllTransactions();

    List<Transaction> getTransactionsByTimestampGreaterThanEqualAndTimestampIsLessThanEqualAndIdEqualsOrderByTimestampDesc(OffsetDateTime dateFrom, OffsetDateTime dateTo, Long id, Pageable pageable);
}
