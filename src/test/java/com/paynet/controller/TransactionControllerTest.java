package com.paynet.controller;

import com.paynet.model.Transaction;
import com.paynet.model.UserPayApp;
import com.paynet.service.BeneficiaryUserService;
import com.paynet.service.TransactionService;
import com.paynet.service.UserService;
import com.paynet.service.ValidationService;
import com.paynet.web.dto.TransactionDTO;
import com.paynet.web.mapper.UserPayAppMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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


    @Test
    @WithMockUser(username = "user")
    public void should_redirect_to_add_transaction_page_successfully() throws Exception {
        //given
        UserPayApp userPayApp = new UserPayApp(6, "erin@gmail.com", "erin",
                1000F, "erin BANK");
        Transaction transaction = new Transaction(6, 1, 15F,
                "dette jeu");
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        List<TransactionDTO> expectedPastTransactionDTOs = new ArrayList<>();
        TransactionDTO pastTransactionDTO = new TransactionDTO("erin@gmail.com",
                "rob@gmail.com", 15F, "dette jeu");
        expectedPastTransactionDTOs.add(pastTransactionDTO);
        List<String> expectedEmailsBeneficiariesUserList = new ArrayList<>();
        expectedEmailsBeneficiariesUserList.add("rob@gmail.com");
        TransactionDTO containerObjectTransactionDTO = new TransactionDTO();
        when(connectedUserInfo.getConnectedUserId(any(Principal.class))).thenReturn(6);
        when(transactionService.findByDebitedAccountId(6)).thenReturn(transactions);
        when(transactionService.roundDownToTheNearestHundredth(any(Float.class))).thenReturn(995.02F);
        when(userService.getEmailById(6)).thenReturn("erin@gmail.com");
        when(userService.getEmailById(1)).thenReturn("rob@gmail.com");
        when(userService.getUserById(6)).thenReturn(userPayApp);
        when(beneficiaryUserService.getEmailsBeneficiariesUserList(6))
                .thenReturn(expectedEmailsBeneficiariesUserList);
        //when
        mockMvc.perform(get("/editTransaction"))
                //then
                .andDo(print())
                .andExpect(view().name("add-transaction"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("transactionDTO"))
                .andExpect(model().attribute("transactionDTO", containerObjectTransactionDTO))
                .andExpect(model().attributeExists("amountMax"))
                .andExpect(model().attribute("amountMax", 995.02F))
                .andExpect(model().attributeExists("pastTransactionDTOs"))
                .andExpect(model().attribute("pastTransactionDTOs", expectedPastTransactionDTOs))
                .andExpect(model().attributeExists("emailsBeneficiariesUserList"))
                .andExpect(model().attribute("emailsBeneficiariesUserList", expectedEmailsBeneficiariesUserList));
    }

    @Test
    @WithMockUser(username = "user")
    public void should_add_transaction_successfully() throws Exception {
        //given
        UserPayApp userPayAppRob = new UserPayApp(1, "rob@gmail.com", "rob", 50F,
                "rob BANK");
        UserPayApp userPayAppErin = new UserPayApp(6, "erin@gmail.com",
                "erin", 5F, "erin BANK");
        TransactionDTO transactionDTO = new TransactionDTO(
                "erin@gmail.com", "rob@gmail.com", 25F,
                "ticket concert");
        Transaction transaction = new Transaction(6, 1, 25F,
                "ticket concert");
        Transaction pastTransaction = new Transaction(6, 1, 15F,
                "dette jeu");
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(pastTransaction);
        transactions.add(transaction);
        TransactionDTO expectedTransactionDTO = new TransactionDTO("erin@gmail.com",
                "rob@gmail.com", 25F, "ticket concert");
        //expectedTransactionDTO.setEmailDebitedAccount("erin@gmail.com");
        List<TransactionDTO> expectedPastTransactionDTOs = new ArrayList<>();
        TransactionDTO pastTransactionDTO = new TransactionDTO("erin@gmail.com",
                "rob@gmail.com", 15F, "dette jeu");
        expectedPastTransactionDTOs.add(pastTransactionDTO);
        expectedPastTransactionDTOs.add(transactionDTO);
        List<String> expectedEmailsBeneficiariesUserList = new ArrayList<>();
        expectedEmailsBeneficiariesUserList.add("rob@gmail.com");
        TransactionDTO containerObjectTransactionDTO = new TransactionDTO();
        when(connectedUserInfo.getConnectedUserId(any(Principal.class))).thenReturn(6);
        when(validationService.validateTransactionDTO(any(TransactionDTO.class))).thenReturn("");
        when(userService.getUserByEmail("rob@gmail.com")).thenReturn(userPayAppRob);
        when(transactionService.findByDebitedAccountId(6)).thenReturn(transactions);
        when(userService.getEmailById(6)).thenReturn("erin@gmail.com");
        when(userService.getEmailById(1)).thenReturn("rob@gmail.com");
        when(userService.getUserById(6)).thenReturn(userPayAppErin);
        when(transactionService.roundDownToTheNearestHundredth(any(Float.class))).thenReturn(4.98F);
        when(beneficiaryUserService.getEmailsBeneficiariesUserList(6))
                .thenReturn(expectedEmailsBeneficiariesUserList);
        //when
        mockMvc.perform(post("/addTransaction").with(csrf())
                        .param("emailCreditedAccount", "rob@gmail.com")
                        .param("amount", "25")
                        .param("description", "ticket concert"))
                //then
                .andDo(print())
                .andExpect(view().name("add-transaction"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("transactionDTO"))
                .andExpect(model().attribute("transactionDTO", containerObjectTransactionDTO))
                .andExpect(model().attributeExists("amountMax"))
                .andExpect(model().attribute("amountMax", 4.98F))
                .andExpect(model().attributeExists("pastTransactionDTOs"))
                .andExpect(model().attribute("pastTransactionDTOs", expectedPastTransactionDTOs))
                .andExpect(model().attributeExists("emailsBeneficiariesUserList"))
                .andExpect(model().attribute("emailsBeneficiariesUserList",
                        expectedEmailsBeneficiariesUserList));
        verify(transactionService).saveTransaction(transaction);
    }

    @Test
    @WithMockUser(username = "user")
    public void should_return_to_add_transaction_page_without_saving_transaction() throws Exception {
        //given
        UserPayApp userPayAppErin = new UserPayApp(6, "erin@gmail.com",
                "erin", 30F, "erin BANK");
        Transaction pastTransaction = new Transaction(6, 1, 15F,
                "dette jeu");
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(pastTransaction);
        List<TransactionDTO> expectedPastTransactionDTOs = new ArrayList<>();
        TransactionDTO pastTransactionDTO = new TransactionDTO("erin@gmail.com",
                "rob@gmail.com", 15F, "dette jeu");
        expectedPastTransactionDTOs.add(pastTransactionDTO);
        List<String> expectedEmailsBeneficiariesUserList = new ArrayList<>();
        expectedEmailsBeneficiariesUserList.add("rob@gmail.com");
        TransactionDTO containerObjectTransactionDTO = new TransactionDTO(null,
                "0", 0F, "");
        when(connectedUserInfo.getConnectedUserId(any(Principal.class))).thenReturn(6);
        when(validationService.validateTransactionDTO(any(TransactionDTO.class)))
                .thenReturn(" Le bénéficiaire doit être renseigné."
                        + "\n La description doit être renseignée."
                        + "\n Le montant ne peut être nul.");
        when(transactionService.findByDebitedAccountId(6)).thenReturn(transactions);
        when(userService.getEmailById(6)).thenReturn("erin@gmail.com");
        when(userService.getEmailById(1)).thenReturn("rob@gmail.com");
        when(transactionService.roundDownToTheNearestHundredth(any(Float.class))).thenReturn(29.86F);
        when(beneficiaryUserService.getEmailsBeneficiariesUserList(6))
                .thenReturn(expectedEmailsBeneficiariesUserList);
        when(userService.getUserById(6)).thenReturn(userPayAppErin);
        //when
        mockMvc.perform(post("/addTransaction").with(csrf())
                        .param("emailCreditedAccount", "0")
                        .param("amount", "0")
                        .param("description", ""))
                //then
                .andDo(print())
                .andExpect(view().name("add-transaction"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("transactionDTO"))
                .andExpect(model().attribute("transactionDTO", containerObjectTransactionDTO))
                .andExpect(model().attributeExists("amountMax"))
                .andExpect(model().attribute("amountMax", 29.86F))
                .andExpect(model().attributeExists("pastTransactionDTOs"))
                .andExpect(model().attribute("pastTransactionDTOs", expectedPastTransactionDTOs))
                .andExpect(model().attributeExists("emailsBeneficiariesUserList"))
                .andExpect(model().attribute("emailsBeneficiariesUserList",
                        expectedEmailsBeneficiariesUserList));
        verify(transactionService, Mockito.times(0)).saveTransaction(any(Transaction.class));
    }

}
