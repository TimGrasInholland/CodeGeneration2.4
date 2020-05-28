package io.swagger.dao;

import io.swagger.model.AccountBalance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountBalanceRepository extends CrudRepository<AccountBalance, Long> {
    AccountBalance getAccountBalanceByAccountId(Long accountId);
}
