package com.paynet.web;

import com.paynet.model.UserPayApp;
import com.paynet.web.dto.UserPayAppDTO;
import com.paynet.web.mapper.UserPayAppMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserPayAppMapperTest {

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @InjectMocks
    UserPayAppMapper userPayAppMapper;

    @Test
    public void should_convert_userPayApp_into_userPayAppDTO_successfully() {
        //given
        UserPayApp userPayApp = new UserPayApp(1, "teva@gmail.com", "passWordTest",
                500, "test bank");
        UserPayAppDTO expectedUserPayAppDTO = new UserPayAppDTO("teva@gmail.com", "test bank");
        //when
        UserPayAppDTO userPayAppDTO = userPayAppMapper.toUserPayAppDTO(userPayApp);
        //then
        assertEquals(expectedUserPayAppDTO, userPayAppDTO);
    }

    @Test
    public void should_convert_userPayAppDTO_into_userPayApp_successfully() {
        //given
        UserPayAppDTO userPayAppDTO = new UserPayAppDTO("teva@gmail.com", "passWordTest",
                "test bank");
        UserPayApp expectedUserPayApp = new UserPayApp("teva@gmail.com", "cryptedPassWordTest",
                "test bank");
        when(bCryptPasswordEncoder.encode("passWordTest")).thenReturn("cryptedPassWordTest");
        //when
        UserPayApp userPayApp = userPayAppMapper.toUserPayApp(userPayAppDTO);
        //then
        assertEquals(expectedUserPayApp, userPayApp);
    }

}
