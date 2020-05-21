package io.swagger.dao;

import io.swagger.model.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    Iterable<Account> findAccountsByUserId(Long userId);

    Account findAccountByIbanEquals(String iban);

    Integer countAccountByIbanEquals(String iban);

    Account findAccountById(Long userId);

    @Query("SELECT COUNT(A) FROM Account AS A")
    Integer countAllAccounts();

    @Query("SELECT A FROM Account AS A WHERE A.iban LIKE ?1")
    List<Account> getAllAccountsWithParams(String iban, Pageable pageable);
}
