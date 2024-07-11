package com.paynet.controller;

import com.paynet.model.Transaction;
import com.paynet.model.UserPayApp;
import com.paynet.service.BeneficiaryUserService;
import com.paynet.service.TransactionService;
import com.paynet.service.UserService;
import com.paynet.service.ValidationService;
import com.paynet.web.dto.BeneficiaryUserDTO;
import com.paynet.web.dto.TransactionDTO;
import com.paynet.web.mapper.UserPayAppMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransactionService transactionService;

    @MockBean
    private ConnectedUserInfo connectedUserInfo;

    @MockBean
    private UserService userService;

    @MockBean
    private ValidationService validationService;
    @MockBean
    private UserPayAppMapper userPayAppMapper;

    @MockBean
    private BeneficiaryUserService beneficiaryUserService;
    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private Principal principal;
    @Mock
    private OidcUser oidcUser;



    //OK!!!
    @Test
    @WithMockUser(username = "user")
    public void should_redirect_to_add_transaction_page_successfully() throws Exception {
        Optional<UserPayApp> userPayApp = Optional.of(new UserPayApp(6,"erin@gmail.com", "erin",
                10,"erin BANK"));
        List<String> expectedEmailsBeneficiariesUserList = new ArrayList<>();
        expectedEmailsBeneficiariesUserList.add("rob@gmail.com");
        TransactionDTO expectedTransactionDTO = new TransactionDTO();
        //expectedTransactionDTO.setEmailDebitedAccount("erin@gmail.com");
        Transaction transaction = new Transaction(6, 1, 15,
                "dette jeu");
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        List<TransactionDTO> expectedPastTransactionDTOs = new ArrayList<>();
        TransactionDTO pastTransactionDTO = new TransactionDTO(
                "erin@gmail.com","rob@gmail.com",15,"dette jeu");
        expectedPastTransactionDTOs.add(pastTransactionDTO);
        when(transactionService.findByDebitedAccountId(6)).thenReturn(transactions);
        when(beneficiaryUserService.getEmailsBeneficiariesUserList(6))
                .thenReturn(expectedEmailsBeneficiariesUserList);
        when(connectedUserInfo.getConnectedUserId(any(Principal.class))).thenReturn(6);
        when(userService.getUserById(6)).thenReturn(userPayApp);
        when(userService.getEmailById(6)).thenReturn("erin@gmail.com");
        when(userService.getEmailById(1)).thenReturn("rob@gmail.com");
          mockMvc.perform(get("/editTransaction"))
            .andDo(print())
            .andExpect(view().name("/add-transaction"))
                  .andExpect(status().is2xxSuccessful())
                  .andExpect(model().attributeExists("transactionDTO"))
                  .andExpect(model().attribute("transactionDTO", expectedTransactionDTO))
                  .andExpect(model().attributeExists("amountMax"))
                  .andExpect(model().attribute("amountMax",9))
                  .andExpect(model().attributeExists("pastTransactionDTOs"))
                  .andExpect(model().attribute("pastTransactionDTOs", expectedPastTransactionDTOs))
                  .andExpect(model().attributeExists("emailsBeneficiariesUserList"))
                  .andExpect(model().attribute("emailsBeneficiariesUserList",
                          expectedEmailsBeneficiariesUserList));
    }


    @Test
    @WithMockUser(username = "user")
    public void should_add_transaction_successfully() throws Exception {
        UserPayApp userPayAppRob = new UserPayApp(1,"rob@gmail.com", "rob",50,
                "rob BANK");
        Optional<UserPayApp> userPayAppErin = Optional.of(new UserPayApp(6,"erin@gmail.com",
                "erin",30,"erin BANK"));
        TransactionDTO transactionDTO = new TransactionDTO(
                "erin@gmail.com","rob@gmail.com",29,
                "ticket concert");
        Transaction transaction = new Transaction(6, 1, 29,
                "ticket concert");
        Transaction pastTransaction = new Transaction(6, 1, 15,
                "dette jeu");
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(pastTransaction);
        transactions.add(transaction);
        List<String> expectedEmailsBeneficiariesUserList = new ArrayList<>();
        expectedEmailsBeneficiariesUserList.add("rob@gmail.com");
        TransactionDTO expectedTransactionDTO = new TransactionDTO();
        //expectedTransactionDTO.setEmailDebitedAccount("erin@gmail.com");
        List<TransactionDTO> expectedPastTransactionDTOs = new ArrayList<>();
        TransactionDTO pastTransactionDTO = new TransactionDTO(
                "erin@gmail.com","rob@gmail.com",15,"dette jeu");
        expectedPastTransactionDTOs.add(pastTransactionDTO);
        expectedPastTransactionDTOs.add(transactionDTO);
        when(connectedUserInfo.getConnectedUserId(any(Principal.class))).thenReturn(6);
        when(validationService.validateTransactionDTO(any(TransactionDTO.class))).thenReturn("");
        when(userService.getUserByEmail("rob@gmail.com")).thenReturn(userPayAppRob);
        when(transactionService.findByDebitedAccountId(6)).thenReturn(transactions);
        when(userService.getEmailById(6)).thenReturn("erin@gmail.com");
        when(userService.getEmailById(1)).thenReturn("rob@gmail.com");
        when(beneficiaryUserService.getEmailsBeneficiariesUserList(6))
        .thenReturn(expectedEmailsBeneficiariesUserList);
        when(userService.getUserById(6)).thenReturn(userPayAppErin);
        mockMvc.perform(post("/addTransaction").with(csrf())
                .param("emailCreditedAccount", "rob@gmail.com")
                .param("amount", "29")
                .param("description", "ticket concert"))
                .andDo(print())
                .andExpect(view().name("/add-transaction"))
                .andExpect(status().is2xxSuccessful())
        .andExpect(model().attributeExists("transactionDTO"))
        .andExpect(model().attribute("transactionDTO", expectedTransactionDTO))
        .andExpect(model().attributeExists("amountMax"))
        .andExpect(model().attribute("amountMax",29))
        .andExpect(model().attributeExists("pastTransactionDTOs"))
        .andExpect(model().attribute("pastTransactionDTOs", expectedPastTransactionDTOs))
        .andExpect(model().attributeExists("emailsBeneficiariesUserList"))
        .andExpect(model().attribute("emailsBeneficiariesUserList",
                expectedEmailsBeneficiariesUserList));
        verify(transactionService).saveTransaction(transaction);
    }


    @Test
    @WithMockUser(username = "user")
    public void should_redirect_to_add_transaction_page_without_saving_transaction() throws Exception {
        Optional<UserPayApp> userPayAppErin = Optional.of(new UserPayApp(6,"erin@gmail.com", "erin",
                30,"erin BANK"));
        List<String> expectedEmailsBeneficiariesUserList = new ArrayList<>();
        expectedEmailsBeneficiariesUserList.add("rob@gmail.com");
        TransactionDTO expectedTransactionDTO = new TransactionDTO(null, "0",
                0, "");
        //expectedTransactionDTO.setEmailDebitedAccount("erin@gmail.com");

        Transaction pastTransaction = new Transaction(6, 1, 15,
                "dette jeu");
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(pastTransaction);

        List<TransactionDTO> expectedPastTransactionDTOs = new ArrayList<>();
        TransactionDTO pastTransactionDTO = new TransactionDTO(
                "erin@gmail.com","rob@gmail.com",15,"dette jeu");
        expectedPastTransactionDTOs.add(pastTransactionDTO);

        when(connectedUserInfo.getConnectedUserId(any(Principal.class))).thenReturn(6);
        when(validationService.validateTransactionDTO(any(TransactionDTO.class)))
                .thenReturn(" Le bénéficiaire doit être renseigné."
                + "\n La description doit être renseignée."
                + "\n Le montant ne peut être nul.");
        when(transactionService.findByDebitedAccountId(6)).thenReturn(transactions);
        when(userService.getEmailById(6)).thenReturn("erin@gmail.com");
        when(userService.getEmailById(1)).thenReturn("rob@gmail.com");
        when(beneficiaryUserService.getEmailsBeneficiariesUserList(6))
                .thenReturn(expectedEmailsBeneficiariesUserList);
        when(userService.getUserById(6)).thenReturn(userPayAppErin);
        mockMvc.perform(post("/addTransaction").with(csrf())
                    .param("emailCreditedAccount", "0")
                    .param("amount", "0")
                    .param("description", ""))
            .andDo(print())
            .andExpect(view().name("/add-transaction"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(model().attributeExists("transactionDTO"))
            .andExpect(model().attribute("transactionDTO", expectedTransactionDTO))
            .andExpect(model().attributeExists("amountMax"))
            .andExpect(model().attribute("amountMax",29))
            .andExpect(model().attributeExists("pastTransactionDTOs"))
            .andExpect(model().attribute("pastTransactionDTOs", expectedPastTransactionDTOs))
            .andExpect(model().attributeExists("emailsBeneficiariesUserList"))
            .andExpect(model().attribute("emailsBeneficiariesUserList",
                    expectedEmailsBeneficiariesUserList));
        verify(transactionService, Mockito.times(0)).saveTransaction(any(Transaction.class));
    }




}
