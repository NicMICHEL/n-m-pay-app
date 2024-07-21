package com.paynet.service;

import com.paynet.model.UserPayApp;
import com.paynet.web.dto.BeneficiaryUserDTO;
import com.paynet.web.dto.TransactionDTO;
import com.paynet.web.dto.UserPayAppDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ValidationServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private ValidationService validationService;

    @Test
    public void should_return_empty_message_when_userPayAppDTO_is_valid() {
        UserPayAppDTO userPayAppDTOToValidate = new UserPayAppDTO("alan@gmail.com", "testPassword",
                "alan Bank");
        UserPayApp userKen = new UserPayApp();
        userKen.setEmail("ken@gmail.com");
        UserPayApp userAda = new UserPayApp();
        userAda.setEmail("ada@gmail.com");
        List<UserPayApp> allUsersList = new ArrayList<>();
        allUsersList.add(userKen);
        allUsersList.add(userAda);
        when(userService.getUsers()).thenReturn(allUsersList);
        String message = validationService.validateUserPayAppDTO(userPayAppDTOToValidate);
        verify(userService).getUsers();
        assertEquals("", message);
    }

    @Test
    public void should_return_appropriate_messages_when_userPayAppDTO_is_not_valid() {
        UserPayAppDTO userPayAppDTOToValidate = new UserPayAppDTO("ada@gmail.com", "",
                "");
        UserPayApp userKen = new UserPayApp();
        userKen.setEmail("ken@gmail.com");
        UserPayApp userAda = new UserPayApp();
        userAda.setEmail("ada@gmail.com");
        List<UserPayApp> allUsersList = new ArrayList<>();
        allUsersList.add(userKen);
        allUsersList.add(userAda);
        when(userService.getUsers()).thenReturn(allUsersList);
        String message = validationService.validateUserPayAppDTO(userPayAppDTOToValidate);
        verify(userService).getUsers();
        assertEquals("L'adresse email ada@gmail.com est déjà utilisée. " +
                "Veuillez en choisir une autre."
                + "\n Votre mot de passe doit être renseigné."
                +"\n Le compte bancaire associé doit être renseigné.", message);
    }

    @Test
    public void should_return_empty_message_when_transactionDTO_is_valid() {
        TransactionDTO transactionDTOToValidate = new TransactionDTO("alan@gmail.com",
                "ken@gmail.com",  10, "remboursement pret");
        String message = validationService.validateTransactionDTO(transactionDTOToValidate);
        assertEquals("", message);
    }

    @Test
    public void should_return_appropriate_messages_when_transactionDTO_is_not_valid() {
        TransactionDTO transactionDTOToValidate = new TransactionDTO("alan@gmail.com",
                "0",  0, "");
        String message = validationService.validateTransactionDTO(transactionDTOToValidate);
        assertEquals(" Le bénéficiaire doit être renseigné."
                + "\n La description doit être renseignée."
                + "\n Le montant ne peut être nul.", message);
    }

    @Test
    public void should_return_empty_message_when_beneficiaryUserDTO_is_valid() {
        BeneficiaryUserDTO beneficiaryUserDTOToValidate = new BeneficiaryUserDTO("alan@gmail.com",
                "ken@gmail.com");
        String message = validationService.validateBeneficiaryUserDTO(beneficiaryUserDTOToValidate);
        assertEquals("", message);
    }

    @Test
    public void should_return_appropriate_message_when_beneficiaryUserDTO_is_not_valid() {
        BeneficiaryUserDTO beneficiaryUserDTOToValidate = new BeneficiaryUserDTO("0",
                "0");
        String message = validationService.validateBeneficiaryUserDTO(beneficiaryUserDTOToValidate);
        assertEquals("Veuillez sélectionner au moins une relation.", message);
    }


}
