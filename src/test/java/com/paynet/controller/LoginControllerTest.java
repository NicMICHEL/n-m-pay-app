package com.paynet.controller;

import com.paynet.repository.UserRepository;
import com.paynet.service.UserService;
import com.paynet.web.mapper.UserPayAppMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ConnectedUserInfo connectedUserInfo;
    @MockBean
    private UserController userController;
    @MockBean
    private TransactionController transactionController;
    @MockBean
    private UserPayAppMapper userPayAppMapper;
    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "user")
    public void should_return_login_page() throws Exception {
        //when
        mockMvc.perform(get("/showLoginPage"))
                //then
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("login"));
    }

}
