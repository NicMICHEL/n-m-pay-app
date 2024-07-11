package com.paynet.service;

import com.paynet.exception.InsufficientAccountBalanceException;
import com.paynet.model.Transaction;
import com.paynet.model.UserPayApp;
import com.paynet.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    TransactionService transactionService;

    @Test
    public void should_save_transaction_successfully() throws InsufficientAccountBalanceException {
        UserPayApp debitedUser = new UserPayApp();
        debitedUser.setIdUser(1);
        debitedUser.setAccountBalance(10);
        UserPayApp creditedUser = new UserPayApp();
        creditedUser.setIdUser(2);
        creditedUser.setAccountBalance(0);
        Transaction transaction = new Transaction();
        transaction.setDebitedAccountId(1);
        transaction.setIdCreditedAccount(2);
        transaction.setAmount(9);
        when(userService.getUserById(transaction.getDebitedAccountId()))
                .thenReturn(Optional.of(debitedUser));
        when(userService.getUserById(transaction.getCreditedAccountId()))
                .thenReturn(Optional.of(creditedUser));
        transactionService.saveTransaction(transaction);
        verify(userService).getUserById(transaction.getDebitedAccountId());
        verify(userService).getUserById(transaction.getCreditedAccountId());
        verify(userService).saveUser(debitedUser);
        verify(userService).saveUser(creditedUser);
        verify(transactionRepository).save(transaction);
        assertEquals(debitedUser.getAccountBalance(), 0);
        assertEquals(creditedUser.getAccountBalance(), 9);
        assertEquals(creditedUser.getIdUser(), transaction.getCreditedAccountId());
        assertEquals(debitedUser.getIdUser(), transaction.getDebitedAccountId());
    }

    @Test
    public void should_throw_an_exception_when_account_balance_is_insufficient() throws InsufficientAccountBalanceException {
        UserPayApp debitedUser = new UserPayApp();
        debitedUser.setIdUser(1);
        debitedUser.setAccountBalance(10);
        UserPayApp creditedUser = new UserPayApp();
        creditedUser.setIdUser(2);
        creditedUser.setAccountBalance(0);
        Transaction transaction = new Transaction();
        transaction.setDebitedAccountId(1);
        transaction.setIdCreditedAccount(2);
        transaction.setAmount(10);
        when(userService.getUserById(transaction.getDebitedAccountId()))
                .thenReturn(Optional.of(debitedUser));
        InsufficientAccountBalanceException thrown = assertThrows(InsufficientAccountBalanceException.class, () -> {
            transactionService.saveTransaction(transaction);
        }, "InsufficientAccountBalanceException was expected");
        assertEquals("Solde insuffisant sur le compte", thrown.getMessage());
        verify(userService).getUserById(transaction.getDebitedAccountId());
        assertEquals(debitedUser.getAccountBalance(), 10);
        assertEquals(creditedUser.getAccountBalance(), 0);
        assertEquals(creditedUser.getIdUser(), transaction.getCreditedAccountId());
        assertEquals(debitedUser.getIdUser(), transaction.getDebitedAccountId());
    }

}
