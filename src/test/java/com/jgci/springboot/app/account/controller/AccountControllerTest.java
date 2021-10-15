package com.jgci.springboot.app.account.controller;

import com.jgci.springboot.app.account.constants.Constants;
import com.jgci.springboot.app.account.entity.Account;
import com.jgci.springboot.app.account.models.Transaction;
import com.jgci.springboot.app.account.service.AccountServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import utils.TestHelper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;


@SpringBootTest
class AccountControllerTest {

    @Mock
    private AccountServiceImpl mockAccountService;

    private Account account;

    @InjectMocks
    private AccountController accountController;

    private static final String VALID_ID = "12345678";
    private static final String JSON_TRANSACTION = "transaction.json";

    @BeforeEach
    public void setUp() {
        account = TestHelper.generateValidAccount();
        Mockito.when(mockAccountService.findById(VALID_ID)).thenReturn(account);
    }

    @Test
    public void getAccountReturnsValidAccountWhenValidIdIsProvided() {
        ResponseEntity<Account> responseEntity = accountController.retrieveAccount(VALID_ID);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().getErrors()).isEmpty();
        Assertions.assertEquals(VALID_ID, responseEntity.getBody().getId());
    }


    @Test
    public void affectAccountReturnsOkStatusWhenValidDepositTransactionDataIsProvided() throws Exception {
        Mockito.when(mockAccountService.affectAccount(eq(account), any(Transaction.class)))
                .thenReturn(Constants.DEPOSIT);

        ResponseEntity<Account> responseEntity = accountController.executeTransfer(TestHelper.toObject(
                TestHelper.getFileText(JSON_TRANSACTION), Transaction.class));

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().getErrors()).isEmpty();
    }

    @Test
    public void affectAccountReturnsOkStatusWhenValidWithdrawalTransactionDataIsProvided() throws Exception {
        Mockito.when(mockAccountService.affectAccount(eq(account), any(Transaction.class)))
                .thenReturn(Constants.WITHDRAWAL);

        ResponseEntity<Account> responseEntity = accountController.executeTransfer(TestHelper.toObject(
                TestHelper.getFileText(JSON_TRANSACTION), Transaction.class));

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().getErrors()).isEmpty();
    }

    @Test
    public void affectAccountReturnsAccountUnchangedWithBadRequestResponseWhenExecuteTransferReturnsSomethingElse() throws Exception {
        Mockito.when(mockAccountService.affectAccount(ArgumentMatchers.any(Account.class), ArgumentMatchers.any(Transaction.class))).thenReturn("Anything");
        ResponseEntity<Account> responseEntity = accountController.executeTransfer(TestHelper.toObject(
                TestHelper.getFileText(JSON_TRANSACTION), Transaction.class));

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        Assertions.assertEquals(account, responseEntity.getBody());
    }


}