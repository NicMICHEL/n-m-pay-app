package com.paynet.web.dto;

import java.util.Objects;

public class BeneficiaryUserDTO {

    private String addBeneficiaryEmail;
    private String suppBeneficiaryEmail;

    public BeneficiaryUserDTO(String addBeneficiaryEmail, String suppBeneficiaryEmail) {
        this.addBeneficiaryEmail = addBeneficiaryEmail;
        this.suppBeneficiaryEmail = suppBeneficiaryEmail;
    }

    public String getAddBeneficiaryEmail() {
        return addBeneficiaryEmail;
    }

    public void setAddBeneficiaryEmail(String addBeneficiaryEmail) {
        this.addBeneficiaryEmail = addBeneficiaryEmail;
    }

    public String getSuppBeneficiaryEmail() {
        return suppBeneficiaryEmail;
    }

    public void setSuppBeneficiaryEmail(String suppBeneficiaryEmail) {
        this.suppBeneficiaryEmail = suppBeneficiaryEmail;
    }

    @Override
    public String toString() {
        return "BeneficiaryUserDTO{" +
                "addBeneficiaryEmail='" + addBeneficiaryEmail + '\'' +
                ", suppBeneficiaryEmail='" + suppBeneficiaryEmail + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BeneficiaryUserDTO that)) return false;
        return Objects.equals(getAddBeneficiaryEmail(), that.getAddBeneficiaryEmail())
                && Objects.equals(getSuppBeneficiaryEmail(), that.getSuppBeneficiaryEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAddBeneficiaryEmail(), getSuppBeneficiaryEmail());
    }

}
