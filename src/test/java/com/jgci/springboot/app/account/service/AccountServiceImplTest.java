package com.jgci.springboot.app.account.service;

import com.jgci.springboot.app.account.constants.Constants;
import com.jgci.springboot.app.account.dao.AccountDao;
import com.jgci.springboot.app.account.entity.Account;
import com.jgci.springboot.app.account.models.Transaction;
import com.jgci.springboot.app.account.exceptions.AccountNotFoundException;
import com.jgci.springboot.app.account.exceptions.MaximumTransactionReachedException;
import com.jgci.springboot.app.account.exceptions.NotEnoughBalanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import utils.TestHelper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@SpringBootTest
class AccountServiceImplTest {

    @Mock
    AccountDao mockAccountDao;

    @InjectMocks
    AccountServiceImpl mockAccountServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        setField(mockAccountServiceImpl, "maxTransactionPerDay", 3);
    }

    private static final String VALID_ID = "12345678";

    @Test
    public void findByIdReturnsValidAccountWhenValidTransactionDataIsProvided() {
        Account account = TestHelper.generateValidAccount();
        when(mockAccountDao.findById(eq(VALID_ID))).thenReturn(Optional.of(account));
        Account result = mockAccountServiceImpl.findById(VALID_ID);

        verify(mockAccountDao, times(1)).findById(eq(VALID_ID));
        assertEquals(account, result);
    }

    @Test
    public void findByIdThrowsAccountNotFoundExceptionNullIdIsProvided() {
        when(mockAccountDao.findById(null)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            mockAccountServiceImpl.findById(null);
        });
    }

    @Test
    public void findByIdThrowsAccountNotFoundExceptionEmptyIdIsProvided() {

        assertThrows(AccountNotFoundException.class, () -> mockAccountServiceImpl.findById(""));
    }

    @Test
    public void findByIdReturnsThrowsAccountNotFoundExceptionWhenNullIdIsProvided() {

        assertThrows(AccountNotFoundException.class, () -> mockAccountServiceImpl.findById(null));
    }

    @Test
    public void affectAccountThrowsMaximumTransactionReachedExceptionWhenTransactionIsWithdrawalAndNumberOfTransactionsInAccountAreThreeOrMore() {
        Account account = TestHelper.generateValidAccount();
        account.setTransfersToday(3);
        Transaction transaction = TestHelper.generateValidTransaction();
        transaction.setDeposit(false);

        assertThrows(MaximumTransactionReachedException.class, () ->
                mockAccountServiceImpl.affectAccount(account, transaction));

    }

    @Test
    public void affectAccountReturnsDepositWhenTransactionIsDepositAndNumberOfTransactionsInAccountAreThreeOrMore() {
        Account account = TestHelper.generateValidAccount();
        account.setTransfersToday(3);
        Transaction transaction = TestHelper.generateValidTransaction();
        int firstSizeOfList = account.getErrors().size();

        assertEquals(Constants.DEPOSIT, mockAccountServiceImpl.affectAccount(account, transaction));
        assertEquals(firstSizeOfList, account.getErrors().size());
    }

    @Test
    public void affectAccountThrowsNotEnoughBalanceExceptionWhenTransactionIsWithdrawalAndAccountBalanceIsLowerThanTransactionAmount() {
        Transaction transaction = TestHelper.generateValidTransaction();
        transaction.setAmount(new BigDecimal(2.00));
        transaction.setDeposit(false);
        Account account = TestHelper.generateValidAccount();
        account.setBalance(new BigDecimal(1.00));

        assertThrows(NotEnoughBalanceException.class, () ->
                mockAccountServiceImpl.affectAccount(account, transaction));
    }


    @Test
    public void affectAccountReturnsWithdrawalWhenTransactionIsNotDepositAndNumberOfTransactionsInAccountAreLessThanThree() {
        Transaction transaction = TestHelper.generateValidTransaction();
        transaction.setDeposit(false);
        Account account = TestHelper.generateValidAccount();
        account.setTransfersToday(2);
        int firstSizeOfList = account.getErrors().size();

        assertEquals(Constants.WITHDRAWAL, mockAccountServiceImpl.affectAccount(account, transaction));
        verify(mockAccountDao, times(1)).save(eq(account));
        assertEquals(firstSizeOfList, account.getErrors().size());
    }

}