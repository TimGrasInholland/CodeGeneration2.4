package io.swagger.service;

import io.swagger.dao.AccountRepository;
import io.swagger.model.Account;
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

    public List<Account> getAccountsByUserId(Long id) {return (List<Account>) accountRepository.findAccountsByUserId(id);}
}
