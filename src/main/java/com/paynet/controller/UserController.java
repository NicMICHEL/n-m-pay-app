package com.paynet.controller;

import com.paynet.model.UserPayApp;
import com.paynet.service.UserService;
import com.paynet.service.ValidationService;
import com.paynet.web.dto.UserPayAppDTO;
import com.paynet.web.mapper.UserPayAppMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.security.Principal;
import java.sql.SQLIntegrityConstraintViolationException;


import static java.lang.Integer.parseInt;

@Controller
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;
/*
    @Autowired
    private BeneficiaryUserService beneficiaryUserService;

    @Autowired
    private TransactionService transactionService;

 */

    @Autowired
    private ValidationService validationService;

    @Autowired
    private UserPayAppMapper userPayAppMapper;
/*
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

 */

    @Autowired
    private IConnectedUserInfo connectedUserInfo;

    @Autowired
    private TransactionController transactionController;

/*
    @PostMapping
    public UserPayApp createUser(@RequestBody UserPayApp userPayApp)
    {
        return userService.saveUser(userPayApp);
    }

 */

    @GetMapping("/inscription")
    public String showInscriptionForm(Principal user, UserPayAppDTO userPayAppDTO, Model model) {
        if (connectedUserInfo.isInstanceOfOAuth2AuthenticationToken(user)) {
            String userInfo = connectedUserInfo.getConnectedUserEmail(user);
            userPayAppDTO.setEmail(userInfo);
            //model.addAttribute("connectedOAuth2AuthenticationEmail", connectedUserInfo.getConnectedUserEmail(user));
        }
        model.addAttribute("userPayAppDTO", userPayAppDTO);
        return "add-userPayApp";
    }

    @PostMapping("/addUserPayApp")
    public String addUserPayApp(@Valid UserPayAppDTO userPayAppDTO, BindingResult result, Model model,
                                Principal user) {
        String err = validationService.validateUserPayAppDTO(userPayAppDTO);
        if (!err.isEmpty()) {
            ObjectError error = new ObjectError("globalError", err);
            result.addError(error);
        }
        if (result.hasErrors()) {
            return "add-userPayApp";
        }
        UserPayApp userPayApp=userPayAppMapper.toUserPayApp(userPayAppDTO);
        userPayApp.setRole("USER");
        userService.saveUser(userPayApp);
        //return transactionController.showAddTransactionForm(model, user);
        return "redirect:/showLoginPage";
    }

    @GetMapping("/editUserPayApp")
    public String showUpdateUserPayAppForm(Model model, Principal user) {
        //setUpdateUserPayApAttributes (model, user);
        int connectedUserId = connectedUserInfo.getConnectedUserId(user);

        UserPayApp userPayApp = userService.getUserById(connectedUserId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + connectedUserId));
        UserPayAppDTO userPayAppDTO = new UserPayAppDTO();
        //userPayAppMapper.toUserPayAppDTO(userPayApp);
        userPayAppDTO =
                userPayAppMapper.toUserPayAppDTO(userPayApp);

        model.addAttribute("userPayAppDTO", userPayAppDTO);
        model.addAttribute("connectedUserEmail", userService.getEmailById(connectedUserId));
        return "update-userPayApp";
    }

    @PostMapping("/updateUserPayApp")
    public String updateUserPayApp(@Valid UserPayAppDTO userPayAppDTO,
                                   BindingResult result, Model model,
                                   Principal user) {
        int connectedUserId = connectedUserInfo.getConnectedUserId(user);
        UserPayApp connectedUserPayApp = userService.getUserById(connectedUserId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + connectedUserId));
        String err = validationService.validateUserPayAppDTO(userPayAppDTO);
        if (!err.isEmpty()) {
            ObjectError error = new ObjectError("globalError", err);
            result.addError(error);
        }
        if (result.hasErrors()) {
            //Renew data
            userPayAppDTO.setEmail(connectedUserPayApp.getEmail());
            //userPayAppDTO.setSelfAccount(userService.getUserById(idUser).get().getSelfAccount());
            model.addAttribute("userPayAppDTO", userPayAppDTO);
            model.addAttribute("connectedUserEmail", userService.getEmailById(connectedUserId));
            return "/update-userPayApp";
        }
        UserPayApp userPayApp=userPayAppMapper.toUserPayApp(userPayAppDTO);
        userPayApp.setEmail(connectedUserPayApp.getEmail());
        userPayApp.setIdUser(connectedUserId);
        userPayApp.setAccountBalance(connectedUserPayApp.getAccountBalance());
        userPayApp.setRole("USER");
        userService.saveUser(userPayApp);

        //setUpdateUserPayApAttributes (model, user);

        //int connectedUserId = connectedUserInfo.getConnectedUserId(user);

        //UserPayApp userPayApp = userService.getUserById(connectedUserId)
        //        .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + connectedUserId));
        //UserPayAppDTO userPayAppDTO2 = new UserPayAppDTO();
        //userPayAppMapper.toUserPayAppDTO(userPayApp);
       // userPayAppDTO =
        //        userPayAppMapper.toUserPayAppDTO(userPayApp);

        model.addAttribute("userPayAppDTO", userPayAppDTO);
        model.addAttribute("connectedUserEmail", userService.getEmailById(connectedUserId));


        return "update-userPayApp";
    }


/*


    public void setUpdateUserPayApAttributes (Model model, Principal user) {
        int connectedUserId = connectedUserInfo.getConnectedUserId(user);

        UserPayApp userPayApp = userService.getUserById(connectedUserId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + connectedUserId));
        UserPayAppDTO userPayAppDTO = new UserPayAppDTO();
                //userPayAppMapper.toUserPayAppDTO(userPayApp);
         userPayAppDTO =
                userPayAppMapper.toUserPayAppDTO(userPayApp);

        model.addAttribute("userPayAppDTO", userPayAppDTO);
        model.addAttribute("connectedUserEmail", userService.getEmailById(connectedUserId));
    }



    @GetMapping("/editTransaction")
    public String showAddtransactionForm(Model model, Principal user, @AuthenticationPrincipal OidcUser oidcUser) {
        int connectedUserId = connectedUserInfo.getConnectedUserId(user, oidcUser);
        UserPayApp userPayApp = userService.getUserById(connectedUserId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + connectedUserId));
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setEmailDebitedAccount(userService.getEmailById(connectedUserId));
        //Each transaction is charged 0.5% of the transaction amount
        //So the maximum amount available for a transaction is the account balance divided by 1.005
        //amountMax = (int)((userService.getUserById(ConnectedUserInfo.connectedUserId).get().getAccountBalance())/1.005);
        setAttributes(model, transactionDTO, user, oidcUser);
        return "add-transaction";
    }

    @PostMapping("/addTransaction")
    public String addTransaction(@Valid TransactionDTO transactionDTO, BindingResult result, Model model,
                                 Principal user, @AuthenticationPrincipal OidcUser oidcUser)
            throws InsufficientAccountBalanceException {
        int connectedUserId = connectedUserInfo.getConnectedUserId(user, oidcUser);
        String err = validationService.validateTransactionDTO(transactionDTO);
        if (!err.isEmpty()) {
            ObjectError error = new ObjectError("globalError", err);
            result.addError(error);
        }
        if (result.hasErrors()) {
            // cf t leaf pdf
            setAttributes(model, transactionDTO, user, oidcUser);
            return "/add-Transaction" ;
        }
        Transaction transaction = new Transaction(connectedUserId,
                userService.getUserByEmail(transactionDTO.getEmailCreditedAccount()).getIdUser(),
                transactionDTO.getAmount(), transactionDTO.getDescription() );
        transactionService.saveTransaction(transaction);
        return showAddtransactionForm(model, user, oidcUser) ;
    }

    private void setAttributes(Model model, TransactionDTO transactionDTO,
                               Principal user, @AuthenticationPrincipal OidcUser oidcUser) {
        int connectedUserId = connectedUserInfo.getConnectedUserId(user, oidcUser);
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
        userService.getUserById(connectedUserId).ifPresent(userPayApp-> {
            int amountMax = (int)(userPayApp.getAccountBalance()/1.005);

            model.addAttribute("amountMax", amountMax);
        } );
        model.addAttribute("pastTransactionDTOs", pastTransactionDTOs);

        model.addAttribute("emailsBeneficiariesUserList",
                beneficiaryUserService.getEmailsBeneficiariesUserList(connectedUserId));

        model.addAttribute("transactionDTO", transactionDTO);
    }
*/
/*
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String handleSQLException(SQLIntegrityConstraintViolationException sqlException) {
        return sqlException.getMessage();
    }

 */
}



