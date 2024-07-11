package com.paynet.service;

import com.paynet.exception.InsufficientAccountBalanceException;
import com.paynet.model.Transaction;
import com.paynet.model.UserPayApp;
import com.paynet.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserService userService;

    public Iterable<Transaction> getTransactions() {return transactionRepository.findAll();}

    public List<Transaction> findByDebitedAccountId(Integer debitedAccountId) {
        return transactionRepository.findByDebitedAccountId(debitedAccountId);
    }

    public Optional<Transaction> getTransactionById (Integer id) {return transactionRepository.findById(id);}

    public Transaction saveTransaction(Transaction transaction) throws InsufficientAccountBalanceException {
        UserPayApp debitedUser = userService.getUserById(transaction.getDebitedAccountId()).get();
        int initialDebitedUserAccountBalance = debitedUser.getAccountBalance();
        //each transaction is charged 0.5% of the transaction amount
        if(transaction.getAmount() <=  (int)(initialDebitedUserAccountBalance/1.005)) {
            debitedUser.setAccountBalance((int) (initialDebitedUserAccountBalance - (transaction.getAmount()*1.005)));
            UserPayApp creditedUser = userService.getUserById(transaction.getCreditedAccountId()).get();
            int initialCreditedUserAccountBalance = creditedUser.getAccountBalance();
            creditedUser.setAccountBalance(initialCreditedUserAccountBalance + transaction.getAmount());
            userService.saveUser(debitedUser);
            userService.saveUser(creditedUser);
            return transactionRepository.save(transaction);
        }
        else {
            throw new InsufficientAccountBalanceException("Solde insuffisant sur le compte");
        }
    }


}


/*

    public Iterable<Transaction> getTransactions() {return transactionRepository.findAll();}

 public void deleteTransaction(Transaction transaction) {transactionRepository.delete(transaction);}

    public void deleteTransactionById(Integer id) {transactionRepository.deleteById(id);}
 */
