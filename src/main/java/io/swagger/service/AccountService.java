package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.model.Account;
import io.swagger.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return (List<Account>) accountRepository.findAll();
    }

    public Account getAccountByIBAN(String iban) {return (Account) accountRepository.findAccountByIbanEquals(iban);}

    public List<Account> getAllAccounts(int offset, int limit) {
        Pageable pageable = new PageRequest(offset, limit);
        Page<Account> page = accountRepository.findAll(pageable);
        return page.getContent();
    }

    public List<Account> getAccountsByUserId(Long id) {
        return (List<Account>) accountRepository.findAccountsByUserId(id);
    }

    public Account findAccountByUserId(Long id){
        return accountRepository.findAccountById(id);
    }

    public void createAccount(Account account) { accountRepository.save(account); }

    public Integer countAccountByIBAN(String iban) {
        return accountRepository.countAccountByIbanEquals(iban);
    }

    @Modifying
    public void disableAccount(Account account) {
        accountRepository.save(account);
    }
}
