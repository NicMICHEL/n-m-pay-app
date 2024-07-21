package com.paynet.controller;

import com.paynet.model.UserPayApp;
import com.paynet.repository.UserRepository;
import com.paynet.web.mapper.UserPayAppMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping
public class LoginController {

    @Autowired
    private IConnectedUserInfo connectedUserInfo;
    @Autowired
    private UserController userController;
    @Autowired
    private TransactionController transactionController;
    @Autowired
    private UserPayAppMapper userPayAppMapper;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String getUserInfo(Principal user, UserPayApp userPayApp, Model model) {
        if (user instanceof OAuth2AuthenticationToken
                && userRepository.findByEmail(connectedUserInfo.getConnectedUserEmail(user)).isEmpty()) {
            return userController.showInscriptionForm(user, userPayAppMapper.toUserPayAppDTO(userPayApp), model);
        } else {
            return transactionController.showAddTransactionForm(model, user);
        }
    }

    @GetMapping("/showLoginPage")
    public String showLoginPage() {
        return "login";
    }

}
