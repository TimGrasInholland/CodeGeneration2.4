package io.swagger.service;

import io.swagger.dao.AccountBalanceRepository;
import io.swagger.model.Account;
import io.swagger.model.AccountBalance;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

@Service
public class AccountBalanceService {
    private final AccountBalanceRepository accountBalanceRepository;

    public AccountBalanceService(AccountBalanceRepository accountBalanceRepository) { this.accountBalanceRepository = accountBalanceRepository; }

    public AccountBalance getAccountBalance(Long accountId) {
        return accountBalanceRepository.getAccountBalanceByAccountId(accountId);
    }

    @Modifying
    public void updateAccountBalance(AccountBalance accountBalance) {
        accountBalanceRepository.save(accountBalance);
    }
}
