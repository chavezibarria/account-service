package com.jgci.springboot.app.account.service;

import com.jgci.springboot.app.account.entity.Account;
import com.jgci.springboot.app.account.models.Transaction;

public interface IAccountService {

    Account findById(String id);

    String affectAccount(Account account, Transaction transaction);

}
