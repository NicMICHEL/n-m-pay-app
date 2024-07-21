package com.paynet.repository;

import com.paynet.model.BeneficiaryUser;
import com.paynet.model.BeneficiaryUserId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BeneficiaryUserRepository extends CrudRepository<BeneficiaryUser, BeneficiaryUserId> {
    List<BeneficiaryUser> findBeneficiaryUsersByIdUser(Integer idUser);

    Optional<BeneficiaryUser> findBeneficiaryUserByIdUserAndIdBeneficiary(Integer idUser, Integer idBeneficiary);
}
