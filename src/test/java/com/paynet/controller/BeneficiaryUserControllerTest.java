package com.paynet.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.paynet.model.BeneficiaryUser;
import com.paynet.model.UserPayApp;
import com.paynet.service.BeneficiaryUserService;
import com.paynet.service.UserService;
import com.paynet.service.ValidationService;
import com.paynet.web.dto.BeneficiaryUserDTO;
import com.paynet.web.dto.UserPayAppDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.parameters.P;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BeneficiaryUserController.class)
public class BeneficiaryUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConnectedUserInfo connectedUserInfo;

    @MockBean
    private BeneficiaryUserService beneficiaryUserService;

    @MockBean
    private UserService userService;

    @MockBean
    private ValidationService validationService;

    @InjectMocks
    private TransactionController transactionController;

    @InjectMocks
    private BeneficiaryUserController beneficiaryUserController;

    //Ok!!!
    @Test
    @WithMockUser(username = "user")
    public void should_redirect_to_beneficiariesUserList_page() throws Exception {
        BeneficiaryUserDTO expectedBeneficiaryUserDTO = new BeneficiaryUserDTO("0",
                "0");

        List<String> expectedEmailsBeneficiariesUserList = new ArrayList<>();
        expectedEmailsBeneficiariesUserList.add("ken@gmail.com");
        expectedEmailsBeneficiariesUserList.add("ada@gmail.com");
        List<String> expectedEmailsNoBeneficiariesUserList = new ArrayList<>();
        expectedEmailsNoBeneficiariesUserList.add("rob@gmail.com");
        expectedEmailsNoBeneficiariesUserList.add("eve@gmail.com");
        when(connectedUserInfo.getConnectedUserId(any(Principal.class)))
                .thenReturn(6);
        when(beneficiaryUserService.getEmailsBeneficiariesUserList(6))
                .thenReturn(expectedEmailsBeneficiariesUserList);
        when(beneficiaryUserService.getEmailsNoBeneficiariesUserList(expectedEmailsBeneficiariesUserList,6))
                .thenReturn(expectedEmailsNoBeneficiariesUserList);
        mockMvc.perform(get("/editBeneficiariesUserList"))
                .andDo(print())
                .andExpect(view().name("beneficiariesUserList"))

                .andExpect(model().attributeExists("beneficiaryUserDTO"))
                .andExpect(model().attribute("beneficiaryUserDTO",expectedBeneficiaryUserDTO))
                .andExpect(model().attributeExists("emailsBeneficiariesUserList"))
                .andExpect(model().attributeExists("emailsNoBeneficiariesUserList"))
                .andExpect(model().attribute("emailsBeneficiariesUserList",expectedEmailsBeneficiariesUserList))
                .andExpect(model().attribute("emailsNoBeneficiariesUserList",expectedEmailsNoBeneficiariesUserList))

                .andExpect(status().is2xxSuccessful());
    }


    @Test
    @WithMockUser(username = "user")
    public void should_modify_BeneficiariesUserList_successfully() throws Exception {
        UserPayApp userRob = new UserPayApp(1,"rob@gmail.com","", 10,"");
        UserPayApp userKen = new UserPayApp(2,"ken@gmail.com","", 10,"");
        BeneficiaryUser addBeneficiaryUser = new BeneficiaryUser(6, 1);
        BeneficiaryUser suppBeneficiaryUser = new BeneficiaryUser(6, 2);

        BeneficiaryUserDTO expectedBeneficiaryUserDTO = new BeneficiaryUserDTO("0",
                "0");
        List<String> expectedEmailsBeneficiariesUserList = new ArrayList<>();
        expectedEmailsBeneficiariesUserList.add("rob@gmail.com");
        List<String> expectedEmailsNoBeneficiariesUserList = new ArrayList<>();
        expectedEmailsNoBeneficiariesUserList.add("ken@gmail.com");

        when(connectedUserInfo.getConnectedUserId(any(Principal.class))).thenReturn(6);
        when(validationService.validateBeneficiaryUserDTO(any(BeneficiaryUserDTO.class))).thenReturn("");
        when(userService.getUserByEmail("rob@gmail.com")).thenReturn(userRob);
        when(userService.getUserByEmail("ken@gmail.com")).thenReturn(userKen);
        when(beneficiaryUserService.getEmailsBeneficiariesUserList(6))
                .thenReturn(expectedEmailsBeneficiariesUserList);
        when(beneficiaryUserService.getEmailsNoBeneficiariesUserList(
                expectedEmailsBeneficiariesUserList,6)).thenReturn(expectedEmailsNoBeneficiariesUserList);
        mockMvc.perform(post("/modifyBeneficiariesUserList").with(csrf())
                        .param("addBeneficiaryEmail", "rob@gmail.com")
                        .param("suppBeneficiaryEmail", "ken@gmail.com"))
                .andDo(print())
                .andExpect(view().name("beneficiariesUserList"))
                .andExpect(model().attributeExists("beneficiaryUserDTO"))
                .andExpect(model().attribute("beneficiaryUserDTO",expectedBeneficiaryUserDTO))
                .andExpect(model().attributeExists("emailsBeneficiariesUserList"))
                .andExpect(model().attributeExists("emailsNoBeneficiariesUserList"))
                .andExpect(model().attribute("emailsBeneficiariesUserList",expectedEmailsBeneficiariesUserList))
                .andExpect(model().attribute("emailsNoBeneficiariesUserList",expectedEmailsNoBeneficiariesUserList))
                .andExpect(status().is2xxSuccessful());

        verify(beneficiaryUserService).saveBeneficiaryUser(addBeneficiaryUser);
        verify(beneficiaryUserService).deleteBeneficiaryUser(suppBeneficiaryUser);
     }

    @Test
    @WithMockUser(username = "user")
    public void should_return_BeneficiariesUserList_without_any_modification() throws Exception {
        BeneficiaryUserDTO expectedBeneficiaryUserDTO = new BeneficiaryUserDTO("0",
                "0");
        List<String> expectedEmailsBeneficiariesUserList = new ArrayList<>();
        expectedEmailsBeneficiariesUserList.add("rob@gmail.com");
        List<String> expectedEmailsNoBeneficiariesUserList = new ArrayList<>();
        expectedEmailsNoBeneficiariesUserList.add("ken@gmail.com");
        //BeneficiaryUser addBeneficiaryUser = new BeneficiaryUser(6, 1);
        //BeneficiaryUser suppBeneficiaryUser = new BeneficiaryUser(6, 2);

        when(connectedUserInfo.getConnectedUserId(any(Principal.class))).thenReturn(6);
        when(validationService.validateBeneficiaryUserDTO(any(BeneficiaryUserDTO.class))).thenReturn(
                "Veuillez sélectionner au moins une relation.");
        when(beneficiaryUserService.getEmailsBeneficiariesUserList(6))
                .thenReturn(expectedEmailsBeneficiariesUserList);
        when(beneficiaryUserService.getEmailsNoBeneficiariesUserList(
                expectedEmailsBeneficiariesUserList,6)).thenReturn(expectedEmailsNoBeneficiariesUserList);
        mockMvc.perform(post("/modifyBeneficiariesUserList").with(csrf())
                        .param("addBeneficiaryEmail", "0")
                        .param("suppBeneficiaryEmail", "0"))
                .andExpect(view().name("beneficiariesUserList"))
                .andExpect(model().attribute("beneficiaryUserDTO",expectedBeneficiaryUserDTO))
                .andExpect(model().attributeExists("emailsBeneficiariesUserList"))
                .andExpect(model().attributeExists("emailsNoBeneficiariesUserList"))
                .andExpect(model().attribute("emailsBeneficiariesUserList",expectedEmailsBeneficiariesUserList))
                .andExpect(model().attribute("emailsNoBeneficiariesUserList",expectedEmailsNoBeneficiariesUserList))
                //.andExpect(model().attributeHasFieldErrorCode())
                //.andExpect(model().errorCount(1))
                .andExpect(status().is2xxSuccessful());
       verify(beneficiaryUserService, Mockito.times(0))
               .saveBeneficiaryUser(any(BeneficiaryUser.class));
       verify(beneficiaryUserService, Mockito.times(0))
               .deleteBeneficiaryUser(any(BeneficiaryUser.class));
    }
     /*
     @Captor
    ArgumentCaptor<Model> modelCaptor;

    @Captor
    ArgumentCaptor<Principal> principalCaptor;



     @Test
    public void should_set_beneficiaryUserList_attributes_successfully() {
         //UserPayApp userRob = new UserPayApp(1,"rob@gmail.com","", 10,"");

         List<String> emailsBeneficiariesUserList = new ArrayList<>();
         emailsBeneficiariesUserList.add("rob@gmail.com");
         List<String> emailsNoBeneficiariesUserList = new ArrayList<>();
         emailsNoBeneficiariesUserList.add("ken@gmail.com");
         BeneficiaryUserDTO beneficiaryUserDTO = new BeneficiaryUserDTO("0", "0");

         when(connectedUserInfo.getConnectedUserId(any(Principal.class))).thenReturn(6);
         when(beneficiaryUserService.getEmailsBeneficiariesUserList(6))
                 .thenReturn(emailsBeneficiariesUserList);
         when(beneficiaryUserService.getEmailsNoBeneficiariesUserList(
                 emailsBeneficiariesUserList,6)).thenReturn(emailsNoBeneficiariesUserList);
         beneficiaryUserController.setBeneficiariesUserListAttributes(modelCaptor.capture(), principalCaptor.capture());
         Model value = modelCaptor.getValue();
         assert(value.containsAttribute("beneficiaryUserDTO"));
         //assert(value. ("beneficiaryUserDTO"));

    }

      */



}
