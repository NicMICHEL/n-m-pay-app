
package com.paynet.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Objects;


@Entity
@Table(name = "b_beneficiary_user")
@DynamicUpdate
@IdClass(BeneficiaryUserId.class)
public class BeneficiaryUser {

    @Id
    @Column(name = "b_fk_id_user")
    private int idUser;

    @Id
    @Column(name = "b_fk_id_beneficiary")
    private int idBeneficiary;

    public BeneficiaryUser() {
    }

    public BeneficiaryUser(int idUser, int idBeneficiary) {
        this.idUser = idUser;
        this.idBeneficiary = idBeneficiary;
    }

    @Override
    public String toString() {
        return "BeneficiaryUser{" +
                "idUser=" + idUser +
                ", idBeneficiary=" + idBeneficiary +
                '}';
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdBeneficiary() {
        return idBeneficiary;
    }

    public void setIdBeneficiary(int idBeneficiary) {
        this.idBeneficiary = idBeneficiary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BeneficiaryUser that)) return false;
        return getIdUser() == that.getIdUser() && getIdBeneficiary() == that.getIdBeneficiary();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdUser(), getIdBeneficiary());
    }
}




