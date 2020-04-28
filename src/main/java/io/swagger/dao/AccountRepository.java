package io.swagger.dao;

import io.swagger.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    public Iterable<Account> findAccountsByUserId(Long userId);
}
