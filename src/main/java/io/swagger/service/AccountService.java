package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.model.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return (List<Account>) accountRepository.findAll();
    }

    public Account getAccountByIBAN(String iban) {
        return accountRepository.findAccountByIbanEqualsAndActiveIsTrue(iban);
    }

    public Account getAccountById(Long id){
        return accountRepository.getAccountById(id);
    }

    public List<Account> getAccountsByUserId(Long id) {
        return (List<Account>) accountRepository.findAccountsByUserIdAndActiveIsTrue(id);
    }

    public Account findAccountById(Long id){
        return accountRepository.findAccountByIdAndActiveIsTrue(id);
    }

    public void createAccount(Account account) {
        accountRepository.save(account);
    }

    public Integer countAccountByIBAN(String iban) {
        return accountRepository.countAccountByIban(iban);
    }

    @Modifying
    public void disableAccount(Account account) {
        accountRepository.save(account);
    }

    public Integer countAllAccounts(){
        return accountRepository.countAllAccountsAndActiveIsTrue();
    }

    public List<Account> getAllAccountsWithParams(Pageable pageable, String iban){
        return accountRepository.getAllAccountsWithParamsAndActiveIsTrue(iban, pageable);
    }
}
