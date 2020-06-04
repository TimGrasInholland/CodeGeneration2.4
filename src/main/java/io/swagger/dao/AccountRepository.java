package io.swagger.dao;

import io.swagger.model.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    @Query("SELECT A FROM Account AS A WHERE A.iban LIKE ?1 AND A.active = true")
    List<Account> getAllAccountsWithParamsAndActiveIsTrue(String iban, Pageable pageable);

    Iterable<Account> findAccountsByUserId(Long userId);

    Account getAccountById(Long id);

    Account findAccountByIbanEquals(String iban);

    Integer countAccountByIban(String iban);
}
