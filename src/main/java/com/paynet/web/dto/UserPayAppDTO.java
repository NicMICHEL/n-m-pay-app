package com.paynet.web.dto;

import java.util.Objects;

public class UserPayAppDTO {

    private String email;

    private String passWord;

    private String selfAccount;

    public UserPayAppDTO() {
    }

    public UserPayAppDTO(String email, String passWord, String selfAccount) {
        this.email = email;
        this.passWord = passWord;
        this.selfAccount = selfAccount;
    }

    public UserPayAppDTO(String email, String selfAccount) {
        this.email = email;
        this.selfAccount = selfAccount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPayAppDTO that)) return false;
        return Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getPassWord(), that.getPassWord())
                && Objects.equals(getSelfAccount(), that.getSelfAccount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getPassWord(), getSelfAccount());
    }

    @Override
    public String toString() {
        return "UserPayAppDTO{" +
                "email='" + email + '\'' +
                ", passWord='" + passWord + '\'' +
                ", selfAccount='" + selfAccount + '\'' +
                '}';
    }

}
