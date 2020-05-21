package io.swagger.dao;

import io.swagger.model.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    Iterable<Account> findAccountsByUserId(Long userId);
    Account findAccountByIbanEquals(String iban);

    Integer countAccountByIbanEquals(String iban);

    List<Account> getAllByIbanContainingIgnoreCase(String iban, Pageable pageable);

    List<Account> findAll(Pageable pageable);

    Account findAccountById(Long userId);
}
