package com.jgci.springboot.app.account.controller;

import com.jgci.springboot.app.account.entity.Account;
import com.jgci.springboot.app.account.models.Transaction;
import com.jgci.springboot.app.account.service.IAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Random;

import static com.jgci.springboot.app.account.constants.Constants.DEPOSIT;
import static com.jgci.springboot.app.account.constants.Constants.WITHDRAWAL;

@Slf4j
@RestController
public class AccountController {

    final private IAccountService accountService;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * @param id a String value that identifies the Account id
     * @return a ResponseEntity with an optional Account object.
     */
    @GetMapping("/get-account/{id}")
    public ResponseEntity<Account> retrieveAccount(@PathVariable String id) {
        Account account = accountService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    /**
     * @return a random Double value in range (1.22-1.28) as String
     * that represents the exchange rate for USD to Canadian Dollars
     */
    @GetMapping("/get-cad-exchange")
    public ResponseEntity<Optional<String>> retrieveCadExchange() {
        Random random = new Random();
        Optional<String> result = Optional.of(String.valueOf(random.doubles(1.22, 1.28)
                .findFirst().getAsDouble()));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    /**
     * @param transaction A custom object that contains:
     *                    - a String value 'id' fields that represents the Account id to be affected
     *                    - a boolean value 'isDeposit' that indicates whether the transaction is a deposit or a withdrawal
     *                    - a BigDecimal value 'amount' that represents the total Amount to be deposited or Withdrawn.
     * @return a ResponseEntity with an optional Account object. status is
     * OK if the Account found was successfully affected with the amount.
     * BAD REQUEST if the Account was not affected due to insufficient-funds or limit-exceeded situations.
     */
    @PostMapping("/execute-transfer")
    public ResponseEntity<Account> executeTransfer(@Valid @RequestBody Transaction transaction) {
        HttpStatus status = HttpStatus.OK;
        Account account = accountService.findById(transaction.getAccountId());
        String transactionResult = accountService.affectAccount(account, transaction);
        if (!DEPOSIT.equalsIgnoreCase(transactionResult) && !WITHDRAWAL.equalsIgnoreCase(transactionResult)) {
            status = HttpStatus.BAD_REQUEST;
        }
        return ResponseEntity.status(status).body(account);
    }


}
