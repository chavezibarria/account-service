package com.jgci.springboot.app.account.dao;

import com.jgci.springboot.app.account.entity.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDao extends CrudRepository<Account, String> {

}
