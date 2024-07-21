package com.paynet.service;

import com.paynet.model.BeneficiaryUser;
import com.paynet.model.UserPayApp;
import com.paynet.repository.BeneficiaryUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class BeneficiaryUserServiceTest {

    @Mock
    private BeneficiaryUserRepository beneficiaryUserRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private BeneficiaryUserService beneficiaryUserService;

    @Test
    public void should_get_emails_beneficiariesUser_list_successfully() {
        //given
        int connectedUserId = 1;
        UserPayApp userPayApp2 = new UserPayApp();
        userPayApp2.setEmail("ken@gmail.com");
        UserPayApp userPayApp3 = new UserPayApp();
        userPayApp3.setEmail("ada@gmail.com");
        BeneficiaryUser beneficiaryUser12 = new BeneficiaryUser(1, 2);
        BeneficiaryUser beneficiaryUser13 = new BeneficiaryUser(1, 3);
        List<BeneficiaryUser> beneficiaryUsers = new ArrayList<>();
        beneficiaryUsers.add(beneficiaryUser12);
        beneficiaryUsers.add(beneficiaryUser13);
        List<String> expectedEmailsBeneficiariesUserList = new ArrayList<>();
        expectedEmailsBeneficiariesUserList.add("ken@gmail.com");
        expectedEmailsBeneficiariesUserList.add("ada@gmail.com");
        when(beneficiaryUserRepository.findBeneficiaryUsersByIdUser(1)).thenReturn(beneficiaryUsers);
        when(userService.getUserById(2)).thenReturn(userPayApp2);
        when(userService.getUserById(3)).thenReturn(userPayApp3);
        //when
        List<String> emailsBeneficiariesUserList = beneficiaryUserService.getEmailsBeneficiariesUserList(connectedUserId);
        //then
        verify(beneficiaryUserRepository).findBeneficiaryUsersByIdUser(connectedUserId);
        verify(userService).getUserById(2);
        verify(userService).getUserById(3);
        assertEquals(expectedEmailsBeneficiariesUserList, emailsBeneficiariesUserList);
    }

    @Test
    public void should_get_emails_no_beneficiariesUser_list_successfully() {
        //given
        int connectedUserId = 1;
        UserPayApp userPayApp1 = new UserPayApp();
        userPayApp1.setEmail("lua@gmail.com");
        UserPayApp userPayApp2 = new UserPayApp();
        userPayApp2.setEmail("ken@gmail.com");
        UserPayApp userPayApp3 = new UserPayApp();
        userPayApp3.setEmail("ada@gmail.com");
        UserPayApp userPayApp4 = new UserPayApp();
        userPayApp4.setEmail("rob@gmail.com");
        UserPayApp userPayApp5 = new UserPayApp();
        userPayApp5.setEmail("eve@gmail.com");
        List<UserPayApp> allUserslist = new ArrayList<>();
        allUserslist.add(userPayApp1);
        allUserslist.add(userPayApp2);
        allUserslist.add(userPayApp3);
        allUserslist.add(userPayApp4);
        allUserslist.add(userPayApp5);
        when(userService.getUsers()).thenReturn(allUserslist);
        List<String> emailsBeneficiariesUserList = new ArrayList<>();
        emailsBeneficiariesUserList.add("ken@gmail.com");
        emailsBeneficiariesUserList.add("ada@gmail.com");
        when(userService.getEmailById(1)).thenReturn("lua@gmail.com");
        List<String> expectedNoEmailsBeneficiariesUserList = new ArrayList<>();
        expectedNoEmailsBeneficiariesUserList.add("rob@gmail.com");
        expectedNoEmailsBeneficiariesUserList.add("eve@gmail.com");
        //when
        List<String> emailsNoBeneficiariesUserList =
                beneficiaryUserService.getEmailsNoBeneficiariesUserList(emailsBeneficiariesUserList, connectedUserId);
        //then
        verify(userService).getEmailById(1);
        verify(userService).getUsers();
        assertEquals(expectedNoEmailsBeneficiariesUserList, emailsNoBeneficiariesUserList);
    }

    @Test
    public void should_throw_an_exception_when_deleting_beneficiaryUser_not_found_in_database() {
        //given
        BeneficiaryUser beneficiaryUser = new BeneficiaryUser(55, 102);
        Optional<BeneficiaryUser> emptyBeneficiaryUser = Optional.empty();
        when(beneficiaryUserRepository.findBeneficiaryUserByIdUserAndIdBeneficiary(55, 102))
                .thenReturn(emptyBeneficiaryUser);
        //when then
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> {
                    beneficiaryUserService.deleteBeneficiaryUser(beneficiaryUser);
                }, "IllegalArgumentException was expected");
        assertEquals("Invalid beneficiaryUser", thrown.getMessage());
    }

}
