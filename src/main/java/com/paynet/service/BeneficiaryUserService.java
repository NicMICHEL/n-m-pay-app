package com.paynet.service;

import com.paynet.model.BeneficiaryUser;
import com.paynet.model.UserPayApp;
import com.paynet.repository.BeneficiaryUserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BeneficiaryUserService {

    @Autowired
    BeneficiaryUserRepository beneficiaryUserRepository;
    @Autowired
    private UserService userService;

    private static final Logger logger = LogManager.getLogger(BeneficiaryUserService.class);
    @Transactional
    public void saveBeneficiaryUser(BeneficiaryUser beneficiaryUser) {
        beneficiaryUserRepository.save(beneficiaryUser);
    }

    public void deleteBeneficiaryUser(BeneficiaryUser beneficiaryUser) throws IllegalArgumentException {
        Optional<BeneficiaryUser> beneficiaryUserToTest =
                beneficiaryUserRepository.findBeneficiaryUserByIdUserAndIdBeneficiary(beneficiaryUser.getIdUser(),
                        beneficiaryUser.getIdBeneficiary());
        if (beneficiaryUserToTest.isPresent()) {
            beneficiaryUserRepository.delete(beneficiaryUser);
        } else {
            logger.error("Unable to find beneficiaryUser corresponding to idUser {} and idBeneficiary {}",
                    beneficiaryUser.getIdUser(), beneficiaryUser.getIdBeneficiary());
            throw new IllegalArgumentException("Invalid beneficiaryUser");
        }
    }

    public List<String> getEmailsBeneficiariesUserList(int connectedUserId) throws IllegalArgumentException {
        List<BeneficiaryUser> beneficiaryUsers
                = beneficiaryUserRepository.findBeneficiaryUsersByIdUser(connectedUserId);
        return beneficiaryUsers.stream().map(beneficiaryUser -> {
            var userPayApp = userService.getUserById(beneficiaryUser.getIdBeneficiary());
            return userPayApp.getEmail();
        }).toList();
    }

    public List<String> getEmailsNoBeneficiariesUserList(List<String> emailsBeneficiariesUserList,
                                                         int connectedUserId) throws IllegalArgumentException {
        List<UserPayApp> allUserslist = userService.getUsers();
        List<String> emailNoBeneficiariesUserList = new ArrayList<>();
        String connectedUserEmail = userService.getEmailById(connectedUserId);
        for (UserPayApp userPayApp : allUserslist) {
            String email = userPayApp.getEmail();
            if ((!emailsBeneficiariesUserList.contains(email)) && (!Objects.equals(connectedUserEmail, email))) {
                emailNoBeneficiariesUserList.add(email);
            }
        }
        return emailNoBeneficiariesUserList;
    }

}
