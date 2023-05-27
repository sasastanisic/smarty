package com.smarty.domain.account.service;

import com.smarty.domain.account.entity.Account;

import java.util.Optional;

public interface AccountService {

    Optional<Account> getAccountByEmail(String email);

    void existsByEmail(String email);

}
