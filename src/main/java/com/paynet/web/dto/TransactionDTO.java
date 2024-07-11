package com.paynet.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.Objects;

public class TransactionDTO {
    //private int debitedAccountId;
    private String emailDebitedAccount;

    private String emailCreditedAccount;

    private int amount;

    private String description;

    public TransactionDTO() {
    }

    public String getEmailDebitedAccount() {
        return emailDebitedAccount;
    }

    public void setEmailDebitedAccount(String emailDebitedAccount) {
        this.emailDebitedAccount = emailDebitedAccount;
    }

    public String getEmailCreditedAccount() {
        return emailCreditedAccount;
    }

    public void setEmailCreditedAccount(String emailCreditedAccount) {
        this.emailCreditedAccount = emailCreditedAccount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TransactionDTO(String emailDebitedAccount, String emailCreditedAccount, int amount, String description) {
        this.emailDebitedAccount = emailDebitedAccount;
        this.emailCreditedAccount = emailCreditedAccount;
        this.amount = amount;
        this.description = description;
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "emailDebitedAccount='" + emailDebitedAccount + '\'' +
                ", emailCreditedAccount='" + emailCreditedAccount + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionDTO that)) return false;
        return getAmount() == that.getAmount() && Objects.equals(getEmailDebitedAccount(), that.getEmailDebitedAccount()) && Objects.equals(getEmailCreditedAccount(), that.getEmailCreditedAccount()) && Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmailDebitedAccount(), getEmailCreditedAccount(), getAmount(), getDescription());
    }

    /*

    public TransactionDTO(int debitedAccountId, String emailCreditedAccount, float amount, String description) {
        this.debitedAccountId = debitedAccountId;
        this.emailCreditedAccount = emailCreditedAccount;
        this.amount = amount;
        this.description = description;
    }

    public int getDebitedAccountId() {
        return debitedAccountId;
    }

    public void setDebitedAccountId(int debitedAccountId) {
        this.debitedAccountId = debitedAccountId;
    }

    public String getEmailCreditedAccount() {
        return emailCreditedAccount;
    }

    public void setEmailCreditedAccount(String emailCreditedAccount) {
        this.emailCreditedAccount = emailCreditedAccount;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "debitedAccountId=" + debitedAccountId +
                ", emailCreditedAccount='" + emailCreditedAccount + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                '}';
    }

     */
}
