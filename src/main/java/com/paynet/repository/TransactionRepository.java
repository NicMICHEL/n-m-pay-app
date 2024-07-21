package com.paynet.repository;

import com.paynet.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    List<Transaction> findByDebitedAccountId(Integer debitedAccountId);

}
