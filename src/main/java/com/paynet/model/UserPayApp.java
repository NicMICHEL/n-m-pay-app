package com.paynet.model;


import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="u_user")
@DynamicUpdate
public class UserPayApp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="u_id")
    private int idUser;

    @Column(name="u_email", unique=true)
    private String email;
    @Column(name="u_password")
    private String passWord;
    @Column(name="u_account_balance")
    private int accountBalance;
    @Column(name="u_self_account")
    private String selfAccount;
    @Column(name = "u_role")
    private String role;

    @OneToMany(

            orphanRemoval = true,
            fetch = FetchType.EAGER, mappedBy = "idUser"
    )
    List<BeneficiaryUser> beneficiariesUser
            = new ArrayList<>();

    public UserPayApp() {
    }

    public UserPayApp(String email, String passWord, String selfAccount) {
        this.email = email;
        this.passWord = passWord;
        this.selfAccount = selfAccount;
    }

    public UserPayApp(int idUser, String email, String passWord, int accountBalance, String selfAccount) {
        this.idUser = idUser;
        this.email = email;
        this.passWord = passWord;
        this.accountBalance = accountBalance;
        this.selfAccount = selfAccount;
    }

    public UserPayApp(int idUser, String email, String passWord, int accountBalance, String selfAccount, String role) {
        this.idUser = idUser;
        this.email = email;
        this.passWord = passWord;
        this.accountBalance = accountBalance;
        this.selfAccount = selfAccount;
        this.role = role;
    }

    public List<BeneficiaryUser> getBeneficiariesUser() {
        return beneficiariesUser;
    }

    public void setBeneficiariesUser(List<BeneficiaryUser> beneficiariesUser) {
        this.beneficiariesUser = beneficiariesUser;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(int accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getSelfAccount() {
        return selfAccount;
    }

    public void setSelfAccount(String selfAccount) {
        this.selfAccount = selfAccount;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // !!!!
    @Override
    public String toString() {
        return "UserPayApp{" +
                "idUser=" + idUser +
                ", email='" + email + '\'' +
                ", passWord='" + passWord + '\'' +
                ", accountBalance=" + accountBalance +
                ", selfAccount='" + selfAccount + '\'' +
                ", role='" + role + '\'' +
                ", beneficiariesUser=" + beneficiariesUser +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPayApp that)) return false;
        return getIdUser() == that.getIdUser() && getAccountBalance() == that.getAccountBalance() && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getPassWord(), that.getPassWord()) && Objects.equals(getSelfAccount(), that.getSelfAccount()) && Objects.equals(getRole(), that.getRole()) && Objects.equals(getBeneficiariesUser(), that.getBeneficiariesUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdUser(), getEmail(), getPassWord(), getAccountBalance(), getSelfAccount(), getRole(), getBeneficiariesUser());
    }
}
