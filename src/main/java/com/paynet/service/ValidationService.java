package com.paynet.service;

import com.paynet.model.UserPayApp;
import com.paynet.web.dto.BeneficiaryUserDTO;
import com.paynet.web.dto.TransactionDTO;
import com.paynet.web.dto.UserPayAppDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ValidationService {

    @Autowired
    private UserService userService;

    public String validateUserPayAppDTO(UserPayAppDTO UserPayAppDTOToValidate) {
        List<UserPayApp> allUsersList = userService.getUsers();
        List<String> emailsUsersList = new ArrayList<>();
        for (UserPayApp userPayApp : allUsersList) {
            emailsUsersList.add(userPayApp.getEmail());
        }
        String message = "";
        if (emailsUsersList.contains(UserPayAppDTOToValidate.getEmail())) {
            message = "L'adresse email " + UserPayAppDTOToValidate.getEmail() + " est déjà utilisée. " +
                    "Veuillez en choisir une autre.";
        }
        if (Objects.equals(UserPayAppDTOToValidate.getEmail(), "")) {
            message = message + "\n Votre mail doit être renseigné.";
        }
        if (Objects.equals(UserPayAppDTOToValidate.getPassWord(), "")) {
            message = message + "\n Votre mot de passe doit être renseigné.";
        }
        if (Objects.equals(UserPayAppDTOToValidate.getSelfAccount(), "")) {
            message = message + "\n Le compte bancaire associé doit être renseigné.";
        }
        return message;
    }

    public String validateTransactionDTO(TransactionDTO transactionDTOToValidate) {
        String message = "";
        if (Objects.equals(transactionDTOToValidate.getEmailCreditedAccount(), "0")) {
            message = " Le bénéficiaire doit être renseigné.";
        }
        if (Objects.equals(transactionDTOToValidate.getDescription(), "")) {
            message = message + "\n La description doit être renseignée.";
        }
        if (transactionDTOToValidate.getAmount() == 0) {
            message = message + "\n Le montant ne peut être nul.";
        }
        return message;
    }

    public String validateBeneficiaryUserDTO(BeneficiaryUserDTO beneficiaryUserDTOToValidate) {
        String message = "";
        if ((Objects.equals(beneficiaryUserDTOToValidate.getAddBeneficiaryEmail(), "0"))
                & (Objects.equals(beneficiaryUserDTOToValidate.getSuppBeneficiaryEmail(), "0"))) {
            message = "Veuillez sélectionner au moins une relation.";
        }
        return message;
    }

}
