package com.smarty.domain.account.service;

import com.smarty.domain.account.entity.Account;
import com.smarty.domain.account.repository.AccountRepository;
import com.smarty.infrastructure.handler.exceptions.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Optional<Account> getAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public void existsByEmail(String email) {
        if (accountRepository.existsByEmail(email)) {
            throw new ConflictException("Account with email %s already exists".formatted(email));
        }
    }

}
