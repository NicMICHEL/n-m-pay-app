package com.paynet.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.io.Serializable;
import java.util.Objects;

public class BeneficiaryUserId implements Serializable {

    private int idUser;

    private int idBeneficiary;

    public BeneficiaryUserId() {
    }

    public BeneficiaryUserId(int idUser, int idBeneficiary) {
        this.idUser = idUser;
        this.idBeneficiary = idBeneficiary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BeneficiaryUserId that)) return false;
        return idUser == that.idUser && idBeneficiary == that.idBeneficiary;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, idBeneficiary);
    }
}
