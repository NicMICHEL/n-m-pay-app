package com.paynet.controller;

import com.paynet.model.BeneficiaryUser;
import com.paynet.service.BeneficiaryUserService;
import com.paynet.service.UserService;
import com.paynet.service.ValidationService;
import com.paynet.web.dto.BeneficiaryUserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping
public class BeneficiaryUserController {

    @Autowired
    private BeneficiaryUserService beneficiaryUserService;
    @Autowired
    private UserService userService;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private ConnectedUserInfo connectedUserInfo;

    @GetMapping("/editBeneficiariesUserList")
    public String showModifyBeneficiaryUserList(Model model, Principal user) {
        BeneficiaryUserDTO beneficiaryUserDTO = new BeneficiaryUserDTO("0", "0");
        setBeneficiariesUserListAttributes(model, user, beneficiaryUserDTO);
        return "beneficiariesUserList";
    }

    public void setBeneficiariesUserListAttributes(Model model, Principal user,
                                                   BeneficiaryUserDTO beneficiaryUserDTO) {
        int connectedUserId = connectedUserInfo.getConnectedUserId(user);
        List<String> emailsBeneficiariesUserList
                = beneficiaryUserService.getEmailsBeneficiariesUserList(connectedUserId);
        model.addAttribute("beneficiaryUserDTO", beneficiaryUserDTO);
        model.addAttribute("emailsNoBeneficiariesUserList",
                beneficiaryUserService.getEmailsNoBeneficiariesUserList(emailsBeneficiariesUserList, connectedUserId));
        model.addAttribute("emailsBeneficiariesUserList", emailsBeneficiariesUserList);
        model.addAttribute("connectedUserEmail", userService.getEmailById(connectedUserId));
    }

    @PostMapping("/modifyBeneficiariesUserList")
    public String modifyBeneficiariesUserList(BeneficiaryUserDTO beneficiaryUserDTO, BindingResult result,
                                              Model model, Principal user) throws IllegalArgumentException {
        int connectedUserId = connectedUserInfo.getConnectedUserId(user);
        String err = validationService.validateBeneficiaryUserDTO(beneficiaryUserDTO);
        if (!err.isEmpty()) {
            ObjectError error = new ObjectError("globalError", err);
            result.addError(error);
        }
        if (result.hasErrors()) {
            setBeneficiariesUserListAttributes(model, user, beneficiaryUserDTO);
            return "beneficiariesUserList";
        }
        String addBeneficiaryEmail = beneficiaryUserDTO.getAddBeneficiaryEmail();
        if (!Objects.equals(addBeneficiaryEmail, "0")) {
            BeneficiaryUser addBeneficiaryUser =
                    new BeneficiaryUser(connectedUserId, userService.getUserByEmail(addBeneficiaryEmail).getIdUser());
            beneficiaryUserService.saveBeneficiaryUser(addBeneficiaryUser);
        }
        String suppBeneficiaryEmail = beneficiaryUserDTO.getSuppBeneficiaryEmail();
        if (!Objects.equals(suppBeneficiaryEmail, "0")) {
            BeneficiaryUser suppBeneficiaryUser =
                    new BeneficiaryUser(connectedUserId, userService.getUserByEmail(suppBeneficiaryEmail).getIdUser());
            beneficiaryUserService.deleteBeneficiaryUser(suppBeneficiaryUser);
        }
        return showModifyBeneficiaryUserList(model, user);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String handleIllegalArgumentException
            (IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException.getMessage();
    }

}
