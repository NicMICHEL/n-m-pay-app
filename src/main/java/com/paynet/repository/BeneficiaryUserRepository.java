
package com.paynet.repository;


import com.paynet.model.BeneficiaryUser;
import com.paynet.model.BeneficiaryUserId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeneficiaryUserRepository extends CrudRepository<BeneficiaryUser, BeneficiaryUserId > {
    List<BeneficiaryUser> findBeneficiaryUserByIdUser(Integer idUser);
}

