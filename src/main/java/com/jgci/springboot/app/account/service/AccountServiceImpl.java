package com.jgci.springboot.app.account.service;

import com.jgci.springboot.app.account.dao.AccountDao;
import com.jgci.springboot.app.account.entity.Account;
import com.jgci.springboot.app.account.models.Transaction;
import com.jgci.springboot.app.account.exceptions.AccountNotFoundException;
import com.jgci.springboot.app.account.exceptions.MaximumTransactionReachedException;
import com.jgci.springboot.app.account.exceptions.NotEnoughBalanceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;

import static com.jgci.springboot.app.account.constants.Constants.*;

@Slf4j
@Service
public class AccountServiceImpl implements IAccountService {

    private AccountDao accountDao;
    @Value("${max.transaction.per.day}")
    private int maxTransactionPerDay;
    public AccountServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    /**
     * @param id String value that identifies the Account id
     * @return an optional Account object.
     */
    @Override
    public Account findById(String id) {
        return accountDao.findById(id).orElseThrow(() -> new AccountNotFoundException("Account '" + id + "' Not found in Database!"));
    }

    /**
     * @param account an Account Object that represents an account that contains:
     *                - 'id' a String field that represents tha account Id
     *                - 'transfersToday' an int field that represents the number of withdrawals made
     *                  from this very account
     *                - 'errors' a List of Strings to be populated by errors occurred during transaction.
     * @param transaction a Transaction that represents the transaction to be made to the account;
     * @return a String value that represents the result of the operation, can be:
     *         - DEPOSIT: if the account was affected with a Deposit.
     *         - WITHDRAWAL: if the account was affected with a Withdrawal.
     *         - LIMIT_EXCEEDED: if the account reached its total withdrawals per day.
     *         - INSUFFICIENT_FUNDS: if the account does not have enough balance for the withdrawal.
     */
    @Override
    public String affectAccount(Account account, Transaction transaction) {
        String result;
        if (transaction.isDeposit()) {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
            log.info("Deposit of [{}] applied to --> [Account::{}].",
                    currencyFormat(transaction.getAmount()), transaction.getAccountId(), account.getTransfersToday());
            result = DEPOSIT;
        } else if (isAccountBalanceGreaterOrEqualThanTransactionAmount(account.getBalance(), transaction.getAmount())) {
            if (account.getTransfersToday() < maxTransactionPerDay) {
                account.setBalance(account.getBalance().subtract(transaction.getAmount()));
                account.setTransfersToday(account.getTransfersToday() + 1);
                log.info("[{}] withdrawn from <-- [Account::{}]. Number of withdrawals today:{}",
                        currencyFormat(transaction.getAmount()), transaction.getAccountId(), account.getTransfersToday());
                result = WITHDRAWAL;
            } else {
                throw new MaximumTransactionReachedException("Account [" + account.getId() + "] has reached its maximum number of withdrawals for today");
            }
        } else {
            throw new NotEnoughBalanceException("Account [" + account.getId() + "] does not have Enough Balance to withdraw");
        }
        accountDao.save(account);
        return result;
    }

    private boolean isAccountBalanceGreaterOrEqualThanTransactionAmount(BigDecimal accountBalance, BigDecimal transactionAmount) {
        return accountBalance.compareTo(transactionAmount) >= 0;
    }

    private String currencyFormat(BigDecimal n) {
        return NumberFormat.getCurrencyInstance().format(n);
    }
}
