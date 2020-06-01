package io.swagger.dao;

import io.swagger.model.AccountBalance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountBalanceRepository extends CrudRepository<AccountBalance, Long> {
    AccountBalance getAccountBalanceByAccountId(Long accountId);
}
