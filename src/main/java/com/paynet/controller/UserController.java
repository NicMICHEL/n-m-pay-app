package com.paynet.controller;

import com.paynet.model.UserPayApp;
import com.paynet.service.UserService;
import com.paynet.service.ValidationService;
import com.paynet.web.dto.UserPayAppDTO;
import com.paynet.web.mapper.UserPayAppMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Controller
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private UserPayAppMapper userPayAppMapper;
    @Autowired
    private IConnectedUserInfo connectedUserInfo;

    @GetMapping("/inscription")
    public String showInscriptionForm(Principal user, UserPayAppDTO userPayAppDTO, Model model) {
        if (connectedUserInfo.isInstanceOfOAuth2AuthenticationToken(user)) {
            String userInfo = connectedUserInfo.getConnectedUserEmail(user);
            userPayAppDTO.setEmail(userInfo);
        }
        model.addAttribute("userPayAppDTO", userPayAppDTO);
        return "add-userPayApp";
    }

    @PostMapping("/addUserPayApp")
    public String addUserPayApp(UserPayAppDTO userPayAppDTO, BindingResult result) {
        String err = validationService.validateUserPayAppDTO(userPayAppDTO);
        if (!err.isEmpty()) {
            ObjectError error = new ObjectError("globalError", err);
            result.addError(error);
        }
        if (result.hasErrors()) {
            return "add-userPayApp";
        }
        UserPayApp userPayApp = userPayAppMapper.toUserPayApp(userPayAppDTO);
        userPayApp.setRole("USER");
        userService.saveUser(userPayApp);
        return "redirect:/showLoginPage";
    }

    @GetMapping("/editUserPayApp")
    public String showUpdateUserPayAppForm(Model model, Principal user) {
        int connectedUserId = connectedUserInfo.getConnectedUserId(user);
        UserPayApp userPayApp = userService.getUserById(connectedUserId);
        UserPayAppDTO userPayAppDTO = userPayAppMapper.toUserPayAppDTO(userPayApp);
        model.addAttribute("userPayAppDTO", userPayAppDTO);
        model.addAttribute("connectedUserEmail", userService.getEmailById(connectedUserId));
        return "update-userPayApp";
    }

    @PostMapping("/updateUserPayApp")
    public String updateUserPayApp(UserPayAppDTO userPayAppDTO, BindingResult result, Model model,
                                   Principal user) {
        int connectedUserId = connectedUserInfo.getConnectedUserId(user);
        UserPayApp connectedUserPayApp = userService.getUserById(connectedUserId);
        String err = validationService.validateUserPayAppDTO(userPayAppDTO);
        if (!err.isEmpty()) {
            ObjectError error = new ObjectError("globalError", err);
            result.addError(error);
        }
        if (result.hasErrors()) {
            userPayAppDTO.setEmail(connectedUserPayApp.getEmail());
            userPayAppDTO.setSelfAccount(connectedUserPayApp.getSelfAccount());
            userPayAppDTO.setPassWord("");
            model.addAttribute("userPayAppDTO", userPayAppDTO);
            model.addAttribute("connectedUserEmail", userService.getEmailById(connectedUserId));
            return "update-userPayApp";
        }
        UserPayApp userPayApp = userPayAppMapper.toUserPayApp(userPayAppDTO);
        userPayApp.setEmail(connectedUserPayApp.getEmail());
        userPayApp.setIdUser(connectedUserId);
        userPayApp.setAccountBalance(connectedUserPayApp.getAccountBalance());
        userPayApp.setRole("USER");
        userService.saveUser(userPayApp);
        userPayAppDTO.setPassWord("");
        model.addAttribute("userPayAppDTO", userPayAppDTO);
        model.addAttribute("connectedUserEmail", userService.getEmailById(connectedUserId));
        return "update-userPayApp";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String handleIllegalArgumentException
            (IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException.getMessage();
    }
}
