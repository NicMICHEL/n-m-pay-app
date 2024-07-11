
package com.paynet.service;


import com.paynet.controller.ConnectedUserInfo;
import com.paynet.model.BeneficiaryUser;
//import com.paynet.model.BeneficiaryUserId;
import com.paynet.model.BeneficiaryUserId;
import com.paynet.model.UserPayApp;
import com.paynet.repository.BeneficiaryUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.security.Principal;
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
    @Autowired
    private ConnectedUserInfo connectedUserInfo;


    public BeneficiaryUser saveBeneficiaryUser(BeneficiaryUser beneficiaryUser) {
        return beneficiaryUserRepository.save(beneficiaryUser);}

    public void deleteBeneficiaryUser(BeneficiaryUser beneficiaryUser) {
        beneficiaryUserRepository.delete(beneficiaryUser);}


    public List<String> getEmailsBeneficiariesUserList(int connectedUserId) {
        List<BeneficiaryUser> beneficiaryUsers
                = beneficiaryUserRepository.findBeneficiaryUserByIdUser(connectedUserId);
        return beneficiaryUsers.stream().map(beneficiaryUser ->{
            var userPayApp = userService.getUserById(beneficiaryUser.getIdBeneficiary());
            if (userPayApp.isPresent()) {
                return userPayApp.get().getEmail();
            }
            return "";
        }).toList();
    }

    public List<String> getEmailsNoBeneficiariesUserList(List<String> emailsBeneficiariesUserList,
                                                         int connectedUserId){

        List<UserPayApp> allUserslist = userService.getUsers();
        List<String> emailNoBeneficiariesUserList = new ArrayList<>();
//exception
        String connectedUserEmail = userService.getEmailById(connectedUserId);
        for (UserPayApp userPayApp : allUserslist) {
            String email = userPayApp.getEmail();
            if ((!emailsBeneficiariesUserList.contains(email))
                    && (!Objects.equals(connectedUserEmail, email))) {
                emailNoBeneficiariesUserList.add(email);
            }
        }
        return emailNoBeneficiariesUserList;
    }

}

/*

    public Iterable<BeneficiaryUser> getBeneficiariesUsers() {return beneficiaryUserRepository.findAll();}

    public List<BeneficiaryUser> getBeneficiariesUser(Integer idUser) {
        return beneficiaryUserRepository.findBeneficiaryUserByIdUser(idUser);}

    // Utilité de getBeneficiaryUserById ? (les deux id fournis sont les 2 seuls champs de l'objet retourné.
    public Optional<BeneficiaryUser> getBeneficiaryUserById(BeneficiaryUserId id) {
        return beneficiaryUserRepository.findById(id);}


    public void deleteBeneficiaryUserById(BeneficiaryUserId id) {
        beneficiaryUserRepository.deleteById(id);}

public List<String> getEmailsBeneficiariesUserList(Principal user, @AuthenticationPrincipal OidcUser oidcUser) {

        int connectedUserId = connectedUserInfo.getConnectedUserId(user, oidcUser);
        List<BeneficiaryUser> beneficiaryUsers
                = beneficiaryUserRepository.findBeneficiaryUserByIdUser(connectedUserId);
        return beneficiaryUsers.stream().map(beneficiaryUser ->{
            var userPayApp = userService.getUserById(beneficiaryUser.getIdBeneficiary());
            if (userPayApp.isPresent()) {
                return userPayApp.get().getEmail();
            }
            return "";
        }).toList();
    }


    public List<String> getEmailsNoBeneficiariesUserList(List<String> emailsBeneficiariesUserList,
                                                         Principal user, @AuthenticationPrincipal OidcUser oidcUser){

        List<UserPayApp> allUserslist = userService.getUsers();
        List<String> emailNoBeneficiariesUserList = new ArrayList<>();
//exception
        String connectedUserEmail = connectedUserInfo.getConnectedUserInfo(user, oidcUser).toString();
        for (UserPayApp userPayApp : allUserslist) {
            String email = userPayApp.getEmail();
            if ((!emailsBeneficiariesUserList.contains(email))
                    && (!Objects.equals(connectedUserEmail, email))) {
                emailNoBeneficiariesUserList.add(email);
            }
        }
        return emailNoBeneficiariesUserList;
    }

     */

