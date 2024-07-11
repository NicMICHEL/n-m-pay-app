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



    public List<UserPayApp> getUsers() {
        return (List<UserPayApp>) userRepository.findAll();
    }

    public Optional<UserPayApp> getUserById(Integer id) {return userRepository.findById(id);}

    @Transactional
    public UserPayApp saveUser(UserPayApp userPayApp) {return userRepository.save(userPayApp);
    }



    // Optional !!!! Il faut vérifier la presence
    public String getEmailById(Integer id) {return this.getUserById(id).get().getEmail();}

    public UserPayApp getUserByEmail(String email) {return userRepository.findByEmail(email);}

}

/*
private static final Logger logger = LogManager.getLogger(UserService.class);

 @Transactional
    public UserPayApp updateUser(UserPayApp userPayApp) {return userRepository.save(userPayApp);
    }
    public void deleteUser(UserPayApp userPayApp) {userRepository.delete(userPayApp);}

    public void deleteUserById(Integer id) {userRepository.deleteById(id);}
 */
