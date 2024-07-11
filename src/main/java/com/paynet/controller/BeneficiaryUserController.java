package com.paynet.controller;

import com.paynet.model.BeneficiaryUser;
import com.paynet.web.dto.BeneficiaryUserDTO;
import com.paynet.service.BeneficiaryUserService;
import com.paynet.service.UserService;
import com.paynet.service.ValidationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

import static java.lang.Integer.parseInt;

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


    @PostMapping("/modifyBeneficiariesUserList")
    public String modifyBeneficiariesUserList(@Valid BeneficiaryUserDTO beneficiaryUserDTO,
                                              BindingResult result, Model model, Principal user) {
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
                    new BeneficiaryUser(connectedUserId,
                            userService.getUserByEmail(addBeneficiaryEmail).getIdUser());
            beneficiaryUserService.saveBeneficiaryUser(addBeneficiaryUser);
        }
        String suppBeneficiaryEmail = beneficiaryUserDTO.getSuppBeneficiaryEmail();
        if (!Objects.equals(suppBeneficiaryEmail, "0")) {
            BeneficiaryUser suppBeneficiaryUser =
                    new BeneficiaryUser(connectedUserId,
                            userService.getUserByEmail(suppBeneficiaryEmail).getIdUser());
            beneficiaryUserService.deleteBeneficiaryUser(suppBeneficiaryUser);
        }
        //setBeneficiariesUserListAttributes(model, user);
        //return "beneficiariesUserList";
        return showModifyBeneficiaryUserList(model, user);
    }


    @GetMapping("/editBeneficiariesUserList")
    public String showModifyBeneficiaryUserList(Model model, Principal user) {
        BeneficiaryUserDTO beneficiaryUserDTO = new BeneficiaryUserDTO("0", "0");
        setBeneficiariesUserListAttributes(model, user, beneficiaryUserDTO);
        return "beneficiariesUserList";
    }

    public void setBeneficiariesUserListAttributes(Model model,
                               Principal user, BeneficiaryUserDTO beneficiaryUserDTO) {
        int connectedUserId = connectedUserInfo.getConnectedUserId(user);
        List<String> emailsBeneficiariesUserList
                = beneficiaryUserService.getEmailsBeneficiariesUserList(connectedUserId);
        //BeneficiaryUserDTO beneficiaryUserDTO = new BeneficiaryUserDTO("0", "0");
        model.addAttribute("beneficiaryUserDTO", beneficiaryUserDTO);
        model.addAttribute("emailsNoBeneficiariesUserList",
                beneficiaryUserService.getEmailsNoBeneficiariesUserList(emailsBeneficiariesUserList, connectedUserId));
        model.addAttribute("emailsBeneficiariesUserList", emailsBeneficiariesUserList);
        model.addAttribute("connectedUserEmail", userService.getEmailById(connectedUserId));
    }

}

/*
@GetMapping("/editBeneficiariesUserList")
public String showModifyBeneficiaryUserList(Model model,
                                            Principal user) {

    int connectedUserId = connectedUserInfo.getConnectedUserId(user);
    List<String> emailsBeneficiariesUserList
            = beneficiaryUserService.getEmailsBeneficiariesUserList(connectedUserId);

    BeneficiaryUserDTO beneficiaryUserDTO = new BeneficiaryUserDTO("0", "0");
    model.addAttribute("beneficiaryUserDTO", beneficiaryUserDTO);
    model.addAttribute("emailsNoBeneficiariesUserList",
            beneficiaryUserService.getEmailsNoBeneficiariesUserList(emailsBeneficiariesUserList, connectedUserId));
    model.addAttribute("emailsBeneficiariesUserList", emailsBeneficiariesUserList);

    return "beneficiariesUserList";
}
 */


/*




    @PostMapping("/addBeneficiaryUser/{idUser}")
    public String addBeneficiaryUser(@PathVariable("idUser") int idUser, @Valid BeneficiaryUser beneficiaryUser,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            beneficiaryUser.setIdUser(idUser);
            return "beneficiariesUserList";
        }
        beneficiaryUserService.saveBeneficiaryUser(beneficiaryUser);
        return "beneficiariesUserList";
    }

 */
/*
    @GetMapping
    public List<BeneficiaryUser> getBeneficiariesUser(@RequestParam Integer idUser) {
        Iterable<BeneficiaryUser> iterable = beneficiaryUserService.getBeneficiariesUser(idUser);
        List<BeneficiaryUser> result = new ArrayList<>();
        for (BeneficiaryUser beneficiaryUser : iterable) {
            result.add(beneficiaryUser);}
        return result;
    }

 */
/*
    @PostMapping
    public BeneficiaryUser saveBeneficiaryUser(@RequestBody BeneficiaryUser beneficiaryUser) {
        return beneficiaryUserService.saveBeneficiaryUser(beneficiaryUser);
    }

 */
/*
    @DeleteMapping
    public void deleteBeneficiaryUser(@RequestBody BeneficiaryUser beneficiaryUser) {
        beneficiaryUserService.deleteBeneficiaryUser(beneficiaryUser);
    }

 */


