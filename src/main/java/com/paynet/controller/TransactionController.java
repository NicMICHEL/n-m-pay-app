package com.paynet.controller;

import com.paynet.exception.InsufficientAccountBalanceException;
import com.paynet.model.Transaction;
import com.paynet.service.BeneficiaryUserService;
import com.paynet.service.TransactionService;
import com.paynet.service.UserService;
import com.paynet.service.ValidationService;
import com.paynet.web.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping()
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ConnectedUserInfo connectedUserInfo;
    @Autowired
    private UserService userService;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private BeneficiaryUserService beneficiaryUserService;

    @GetMapping("/editTransaction")
    public String showAddTransactionForm(Model model, Principal user) throws IllegalArgumentException {
        TransactionDTO transactionDTO = new TransactionDTO();
        setAddTransactionAttributes(model, transactionDTO, user);
        return "add-transaction";
    }

    @PostMapping("/addTransaction")
    public String addTransaction(TransactionDTO transactionDTO, BindingResult result, Model model,
                                 Principal user) throws InsufficientAccountBalanceException, IllegalArgumentException {
        int connectedUserId = connectedUserInfo.getConnectedUserId(user);
        String err = validationService.validateTransactionDTO(transactionDTO);
        if (!err.isEmpty()) {
            ObjectError error = new ObjectError("globalError", err);
            result.addError(error);
        }
        if (result.hasErrors()) {
            setAddTransactionAttributes(model, transactionDTO, user);
            return "add-transaction";
        }
        Transaction transaction = new Transaction(connectedUserId,
                userService.getUserByEmail(transactionDTO.getEmailCreditedAccount()).getIdUser(),
                transactionDTO.getAmount(), transactionDTO.getDescription());
        transactionService.saveTransaction(transaction);
        return showAddTransactionForm(model, user);
    }

    private void setAddTransactionAttributes(Model model, TransactionDTO transactionDTO, Principal user)
            throws IllegalArgumentException {
        int connectedUserId = connectedUserInfo.getConnectedUserId(user);
        List<Transaction> transactions
                = transactionService.findByDebitedAccountId(connectedUserId);
        List<TransactionDTO> pastTransactionDTOs = new ArrayList<>();
        for (Transaction transaction : transactions) {
            TransactionDTO transactionDTOtoAdd = new TransactionDTO(
                    userService.getEmailById(transaction.getDebitedAccountId()),
                    userService.getEmailById(transaction.getCreditedAccountId()),
                    transaction.getAmount(),
                    transaction.getDescription());
            pastTransactionDTOs.add(transactionDTOtoAdd);
        }
        //Each transaction is charged 0.5% of the transaction amount
        //So the maximum amount available for a transaction is the account balance divided by 1.005
        float amountMax = transactionService.roundDownToTheNearestHundredth(
                (float) ((userService.getUserById(connectedUserId).getAccountBalance()) / 1.005));
                model.addAttribute("amountMax", amountMax);
        model.addAttribute("pastTransactionDTOs", pastTransactionDTOs);
        model.addAttribute("emailsBeneficiariesUserList",
                beneficiaryUserService.getEmailsBeneficiariesUserList(connectedUserId));
        model.addAttribute("transactionDTO", transactionDTO);
        model.addAttribute("connectedUserEmail", userService.getEmailById(connectedUserId));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String handleInsufficientAccountBalanceException
            (InsufficientAccountBalanceException insufficientAccountBalanceException) {
        return insufficientAccountBalanceException.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String handleIllegalArgumentException
            (IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException.getMessage();
    }

}
