package com.paynet.service;

import com.paynet.model.UserPayApp;
import com.paynet.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LogManager.getLogger(UserService.class);

    public List<UserPayApp> getUsers() {
        return (List<UserPayApp>) userRepository.findAll();
    }

    public UserPayApp getUserById(Integer id) throws IllegalArgumentException {
        Optional<UserPayApp> userPayApp = userRepository.findById(id);
        if (userPayApp.isPresent()) {
            return userPayApp.get();
        } else {
            logger.error("Unable to find userPayApp corresponding to id {}", id);
            throw new IllegalArgumentException("Invalid user id");
        }
    }

    @Transactional
    public void saveUser(UserPayApp userPayApp) {
        userRepository.save(userPayApp);
    }

    public String getEmailById(Integer id) throws IllegalArgumentException {
        return this.getUserById(id).getEmail();
    }

    public UserPayApp getUserByEmail(String email) throws IllegalArgumentException {
        Optional<UserPayApp> userPayApp = userRepository.findByEmail(email);
        if (userPayApp.isPresent()) {
            return userPayApp.get();
        } else {
            logger.error("Unable to find userPayApp corresponding to email {}", email);
            throw new IllegalArgumentException("Invalid user email");
        }
    }

}
