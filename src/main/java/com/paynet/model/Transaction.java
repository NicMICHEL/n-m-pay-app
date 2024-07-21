package com.paynet.model;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Objects;

@Entity
@Table(name = "t_transaction")
@DynamicUpdate
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "t_id")
    private int idTransaction;
    @Column(name = "t_debited_account")
    private int debitedAccountId;
    @Column(name = "t_credited_account")
    private int creditedAccountId;
    @Column(name = "t_amount")
    private int amount;
    @Column(name = "t_description")
    private String description;

    public Transaction() {
    }

    public Transaction(int debitedAccountId, int creditedAccountId, int amount, String description) {
        this.debitedAccountId = debitedAccountId;
        this.creditedAccountId = creditedAccountId;
        this.amount = amount;
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public int getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(int idTransaction) {
        this.idTransaction = idTransaction;
    }

    public int getDebitedAccountId() {
        return debitedAccountId;
    }

    public void setDebitedAccountId(int idDebitedAccount) {
        this.debitedAccountId = idDebitedAccount;
    }

    public int getCreditedAccountId() {
        return creditedAccountId;
    }

    public void setIdCreditedAccount(int creditedAccountId) {
        this.creditedAccountId = creditedAccountId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "idTransaction=" + idTransaction +
                ", debitedAccountId=" + debitedAccountId +
                ", creditedAccountId=" + creditedAccountId +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return getIdTransaction() == that.getIdTransaction() && getDebitedAccountId() == that.getDebitedAccountId()
                && getCreditedAccountId() == that.getCreditedAccountId() && getAmount() == that.getAmount()
                && Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdTransaction(), getDebitedAccountId(), getCreditedAccountId(), getAmount(),
                getDescription());
    }

}
