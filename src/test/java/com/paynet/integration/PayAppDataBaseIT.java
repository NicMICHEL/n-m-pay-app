package com.paynet.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.paynet.NMPayAppApplication;
import com.paynet.controller.TransactionController;
import com.paynet.controller.UserController;

import com.paynet.controller.ConnectedUserInfo;
import com.paynet.controller.UserController;
import com.paynet.model.BeneficiaryUser;
import com.paynet.model.Transaction;
import com.paynet.model.UserPayApp;
import com.paynet.repository.BeneficiaryUserRepository;
import com.paynet.service.BeneficiaryUserService;
import com.paynet.service.TransactionService;
import com.paynet.service.UserService;

import com.paynet.service.ValidationService;
import com.paynet.web.dto.TransactionDTO;
import com.paynet.web.dto.UserPayAppDTO;
import com.paynet.web.mapper.UserPayAppMapper;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import org.junit.Test;
//import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;


//@WebMvcTest(UserController.class)

//@ExtendWith(MockitoExtension.class)
//@ContextConfiguration()


//@RunWith(SpringRunner.class)
/*
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = NMPayAppApplication.class)

 */
//@AutoConfigureMockMvc
/*
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@ContextConfiguration()
@ExtendWith(SpringExtension.class)
@WebAppConfiguration

 */
/*

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = NMPayAppApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")


 */
@RunWith(SpringRunner.class)
//@SpringBootTest(
//        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
//        classes = NMPayAppApplication.class)
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(locations = "classpath:application-test.properties")
@TestPropertySource(
        locations = "classpath:application-test.properties")
@WebMvcTest(controllers = {TransactionController.class})
//@WebAppConfiguration(value = "")
public class PayAppDataBaseIT {

    //@Autowired
    //private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private ValidationService validationService;
    @MockBean
    private UserPayAppMapper userPayAppMapper;
    @MockBean
    private ConnectedUserInfo connectedUserInfo;
    @MockBean
    private TransactionController transactionController;

    @MockBean
    private BeneficiaryUserRepository beneficiaryUserRepository;
    private static TransactionService transactionService;

    private static BeneficiaryUserService beneficiaryUserService;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(username = "user")
    public void should_add_transaction_successfully() throws Exception {


        Transaction expectedTransaction = new Transaction(6, 4, 9,
                "ticket concert");
        when(connectedUserInfo.getConnectedUserId(any(Principal.class))).thenReturn(2);
//when(transactionController.showAddTransactionForm(any(), any())).thenReturn();
        mockMvc.perform(post("/addTransaction").with(csrf())
                        .param("emailCreditedAccount", "aria@gmail.com")
                        .param("amount", "9")
                        .param("description", "ticket concert"))
                .andDo(print());

        assertEquals(expectedTransaction, transactionService.getTransactionById(3).get());
    }

    @Test
    @WithMockUser(username = "user")
    public void should_save_valid_userPayApp() throws Exception {
        UserPayApp expectedUserPayApp = new UserPayApp("erin@gmail.com", "erin_password",
                "erin_selfAccount");

        //when(validationService.validateUserPayAppDTO(any(UserPayAppDTO.class))).thenReturn("");
        //when(userPayAppMapper.toUserPayApp(any(UserPayAppDTO.class))).thenReturn(userPayApp);
        UserPayAppDTO userPayAppDTO = new UserPayAppDTO("", "",
                "");

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/addUserPayApp").with(csrf())
                        .content(objectMapper.writeValueAsBytes(userPayAppDTO))
                        .param("email", "erin@gmail.com")

                        .param("passWord", "erin_password")
                        .param("selfAccount", "erin_selfAccount"));
                //.andExpect(status().is3xxRedirection())
                //.andExpect(redirectedUrl("/login"));
        // .andExpect(view().name("add-transaction"));
        // then
        // .andExpect(view().name("update-userPayApp"))
        //.andExpect(model().attributeExists("userPayAppDTO"))
        //.andExpect(model().attribute("userPayAppDTO",expectedUserPayAppDTO))
        //.andExpect(status().is2xxSuccessful());

        //verify(userService).saveUser(userPayApp);
        assertEquals(expectedUserPayApp, userService.getUserById(5).get());

    }
    @Test
    @WithMockUser(username = "user")
    public void should_save_updated_userPayApp_successfully() throws Exception {
        UserPayApp connectedUserPayApp = new UserPayApp(2,"lola@gmail.com", "lola",
                2000,"BK lola", "user");
        UserPayApp expectedUserPayApp = new UserPayApp(2,"lola@gmail.com", "lola",
                2000,"count", "user");

        UserPayAppDTO attributeUserPayAppDTO = new UserPayAppDTO("lola@gmail.com", "lola",
                "BK lola");
        UserPayAppDTO userPayAppDTO = new UserPayAppDTO("erin@gmail.com", "erin_password",
                "erin_selfAccount");
        UserPayApp userPayApp = new UserPayApp("erin@gmail.com", "erin_password",
                "erin_selfAccount");
        ObjectMapper objectMapper = new ObjectMapper();


        when(connectedUserInfo.getConnectedUserId(any(Principal.class))).thenReturn(2);
        //when(userService.getUserById(2)).thenReturn(Optional.of(connectedUserPayApp));
        //when(validationService.validateUserPayAppDTO(any(UserPayAppDTO.class))).thenReturn("");

        when(userPayAppMapper.toUserPayApp(userPayAppDTO)).thenReturn(userPayApp);
        mockMvc.perform(post("/updateUserPayApp").with(csrf())
                        .content(objectMapper.writeValueAsBytes(attributeUserPayAppDTO))
                        .param("email", "lola@gmail.com")
                        .param("passWord", "lola")
                        .param("selfAccount", "count"));
        assertEquals(expectedUserPayApp, userService.getUserById(2).get());
    }


}
/*
    @Test
    @WithMockUser(username = "user")
    public void should_add_beneficiaryUser_successfully() throws Exception {


        //BeneficiaryUser expectedBeneficiaryUser = new BeneficiaryUser(3, 4);
       when(connectedUserInfo.getConnectedUserId(any(Principal.class))).thenReturn(2);

        mockMvc.perform(post("/modifyBeneficiariesUserList").with(csrf())
                        .param("addBeneficiaryEmail", "greg@gmail.com")
                        .param("suppBeneficiaryEmail", "aria@gmail.com"))
                .andDo(print());



        //assertEquals(beneficiaryUserService.getBeneficiaryUserById(), expectedTransaction     );
    }



    @Test
    public void should_create_userPayApp_successfully() throws Exception {
                mockMvc.perform(post("/addUserPayApp").with(csrf())
                        .param("email", "bix@gmail.com")
                        .param("passWord", "bix")
                        .param("selfAccount", "bix Bank"));

        UserPayApp userPayAppBix = new UserPayApp("bix@gmail.com", "bix", "bix Bank");
        userService.saveUser(userPayAppBix);
        // .andExpect(status().is2xxSuccessful());
       //assertEquals(userService.getUserById(5).get().getEmail(), "bix@gmail.com"     );
        }
*/





/*
                andExpect(content().string(containsString("id=\"solution\""))).
                andExpect(content().string(containsString(">5</span>")));

        verify(calculator).add(2, 3);
        verify(solutionFormatter).format(5);

         */


/*
    @Test
    public void should_create_transaction_successfully(){


        Iterable<Transaction> iterableTransactions=transactionService.getTransactions();
        iterableTransactions.l
        transactionService.getTransactionById(3).get()

        ConnectedUserInfo.connectedUserId = 2;
        TransactionDTO transactionDTO = new TransactionDTO("lola@gmail.com",
                "aria@gmail.com", 10, "pari perdu");
    }
    */