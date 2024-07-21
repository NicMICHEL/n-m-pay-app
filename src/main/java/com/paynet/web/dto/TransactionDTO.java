package com.paynet.web.dto;

import java.util.Objects;

public class TransactionDTO {
    private String emailDebitedAccount;
    private String emailCreditedAccount;
    private float amount;
    private String description;

    public TransactionDTO() {
    }

    public TransactionDTO(String emailDebitedAccount, String emailCreditedAccount, float amount, String description) {
        this.emailDebitedAccount = emailDebitedAccount;
        this.emailCreditedAccount = emailCreditedAccount;
        this.amount = amount;
        this.description = description;
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
        return getAmount() == that.getAmount() && Objects.equals(getEmailDebitedAccount(), that.getEmailDebitedAccount())
                && Objects.equals(getEmailCreditedAccount(), that.getEmailCreditedAccount())
                && Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmailDebitedAccount(), getEmailCreditedAccount(), getAmount(), getDescription());
    }

}
