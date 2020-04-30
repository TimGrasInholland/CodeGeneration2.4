package io.swagger.dao;

import io.swagger.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository; //OLD ONE
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {

    Iterable<Account> findAccountsByUserId(Long userId);

    Account findAccountByIbanEquals(String iban);
    
    Integer countAccountByIbanEquals(String iban);

    //find all accounts with query values
    public Page<Account> findAll(Pageable pageable);

    Account findAccountById(Long userId);
}
