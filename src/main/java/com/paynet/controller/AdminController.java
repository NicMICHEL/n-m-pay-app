/*package com.paynet.controller;

import com.paynet.model.UserPayApp;
import com.paynet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/user")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Iterable<UserPayApp> getUsers() {
        return userService.getUsers();
    }
}

 */
