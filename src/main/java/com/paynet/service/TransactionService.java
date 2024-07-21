package com.paynet.service;

import com.paynet.exception.InsufficientAccountBalanceException;
import com.paynet.model.Transaction;
import com.paynet.model.UserPayApp;
import com.paynet.repository.TransactionRepository;
import com.paynet.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    private static final Logger logger = LogManager.getLogger(TransactionService.class);

    public Iterable<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> findByDebitedAccountId(Integer debitedAccountId) throws IllegalArgumentException {
        Optional<UserPayApp> userPayApp = userRepository.findById(debitedAccountId);
        if (userPayApp.isPresent()) {
            return transactionRepository.findByDebitedAccountId(debitedAccountId);
        } else {
            logger.error("Unable to find userPayApp corresponding to debitedAccount Id {}", debitedAccountId);
            throw new IllegalArgumentException("Invalid debitedAccount Id");
        }
    }

    public Optional<Transaction> getTransactionById(Integer id) {
        return transactionRepository.findById(id);
    }
    @Transactional
    public void saveTransaction(Transaction transaction) throws InsufficientAccountBalanceException,
            IllegalArgumentException {
        UserPayApp debitedUser = userService.getUserById(transaction.getDebitedAccountId());
        int initialDebitedUserAccountBalance = debitedUser.getAccountBalance();
        //each transaction is charged 0.5% of the transaction amount
        if (transaction.getAmount() <= (int) (initialDebitedUserAccountBalance / 1.005)) {
            debitedUser.setAccountBalance((int) (initialDebitedUserAccountBalance - (transaction.getAmount() * 1.005)));
            UserPayApp creditedUser = userService.getUserById(transaction.getCreditedAccountId());
            int initialCreditedUserAccountBalance = creditedUser.getAccountBalance();
            creditedUser.setAccountBalance(initialCreditedUserAccountBalance + transaction.getAmount());
            userService.saveUser(debitedUser);
            userService.saveUser(creditedUser);
            transactionRepository.save(transaction);
        } else {
            throw new InsufficientAccountBalanceException("Solde insuffisant sur le compte");
        }
    }

}
