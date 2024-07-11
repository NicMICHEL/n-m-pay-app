package com.paynet.web.mapper;

import com.paynet.model.UserPayApp;
import com.paynet.web.dto.UserPayAppDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserPayAppMapper {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public UserPayAppDTO toUserPayAppDTO(UserPayApp userPayApp) {
        return new UserPayAppDTO(userPayApp.getEmail(), userPayApp.getSelfAccount());}

    public UserPayApp toUserPayApp(UserPayAppDTO userPayAppDTO) {
        return new UserPayApp(userPayAppDTO.getEmail(),
                bCryptPasswordEncoder.encode(userPayAppDTO.getPassWord()),
                userPayAppDTO.getSelfAccount());
    }

}
