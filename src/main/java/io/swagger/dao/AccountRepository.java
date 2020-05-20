package io.swagger.dao;

import io.swagger.model.Account;
import io.swagger.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {

    Iterable<Account> findAccountsByUserId(Long userId);
    Account findAccountByIbanEquals(String iban);

    Integer countAccountByIbanEquals(String iban);

    List<Account> getAllByIbanContainingIgnoreCase(String iban, Pageable pageable);

    Page<Account> findAll(Pageable pageable);

    Account findAccountById(Long userId);
}
