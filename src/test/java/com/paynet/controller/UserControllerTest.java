
package com.paynet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paynet.model.UserPayApp;
import com.paynet.service.UserService;
import com.paynet.service.ValidationService;
import com.paynet.web.dto.UserPayAppDTO;
import com.paynet.web.mapper.UserPayAppMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

//@TestPropertySource("/application-test.properties")
//@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
//@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private UserPayAppMapper userPayAppMapper;
    @MockBean
    private ConnectedUserInfo connectedUserInfo;
    @InjectMocks
    private UserController userController;
    @MockBean
    private ValidationService validationService;
    @MockBean
    private TransactionController transactionController;

    //@MockBean
    //BindingResult bindingResult;

    //ok !!!!
    @Test
    @WithMockUser(username = "user")
    public void should_redirect_to_add_userPayApp_page_without_prepopulated_email() throws Exception {
        //given
        UserPayAppDTO expectedUserPayAppDTO = new UserPayAppDTO();
        when(connectedUserInfo.isInstanceOfOAuth2AuthenticationToken(any(Principal.class)))
                .thenReturn(false);
        //when
        mockMvc.perform(get("/inscription"))
                //then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("add-userPayApp"))
                .andExpect(model().attributeExists("userPayAppDTO"))
                .andExpect(model().attribute("userPayAppDTO", expectedUserPayAppDTO));
    }

    //ok !!!!
    @Test
    @WithMockUser(username = "user")
    public void should_redirect_to_add_userPayApp_page_with_prepopulated_email() throws Exception {
        //given
        UserPayAppDTO expectedUserPayAppDTO = new UserPayAppDTO();
        expectedUserPayAppDTO.setEmail("erin@gmail.com");
        when(connectedUserInfo.isInstanceOfOAuth2AuthenticationToken(any(Principal.class)))
                .thenReturn(true);
        when(connectedUserInfo.getConnectedUserEmail(any(Principal.class)))
                .thenReturn("erin@gmail.com");
        //when
        mockMvc.perform(get("/inscription"))
                //then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("add-userPayApp"))
                .andExpect(model().attributeExists("userPayAppDTO"))
                .andExpect(model().attribute("userPayAppDTO", expectedUserPayAppDTO));
    }

//ok!!
    @Test
    @WithMockUser(username = "user")
    public void should_save_valid_userPayApp() throws Exception {
        UserPayApp userPayApp = new UserPayApp("erin@gmail.com", "erin_password",
                "erin_selfAccount");

        when(validationService.validateUserPayAppDTO(any(UserPayAppDTO.class))).thenReturn("");
        when(userPayAppMapper.toUserPayApp(any(UserPayAppDTO.class))).thenReturn(userPayApp);
        UserPayAppDTO userPayAppDTO = new UserPayAppDTO("", "",
                "");

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/addUserPayApp").with(csrf())
                        .content(objectMapper.writeValueAsBytes(userPayAppDTO))
                .param("email", "erin@gmail.com")

                .param("passWord", "erin_password")
                .param("selfAccount", "erin_selfAccount"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/showLoginPage"));
               // .andExpect(view().name("add-transaction"));
                // then
               // .andExpect(view().name("update-userPayApp"))
                //.andExpect(model().attributeExists("userPayAppDTO"))
                //.andExpect(model().attribute("userPayAppDTO",expectedUserPayAppDTO))
                //.andExpect(status().is2xxSuccessful());

        verify(userService).saveUser(userPayApp);

    }

    //ok!!!
    @Test
    @WithMockUser(username = "user")
    public void should_redirect_to_update_userPayApp_page() throws Exception {
        // given
        Optional<UserPayApp> userPayApp = Optional.of(new UserPayApp("erin@gmail.com", "erin", "erin K"));
        UserPayAppDTO expectedUserPayAppDTO = new UserPayAppDTO("erin@gmail.com", "erin", "erin K");
        when(connectedUserInfo.getConnectedUserId(any(Principal.class))).thenReturn(6);
        when(userService.getUserById(6)).thenReturn(userPayApp);
        when(userPayAppMapper.toUserPayAppDTO(userPayApp.get())).thenReturn(expectedUserPayAppDTO);
        // when
        mockMvc.perform(get("/editUserPayApp"))
                // then
                .andExpect(view().name("update-userPayApp"))
                .andExpect(model().attributeExists("userPayAppDTO"))
                .andExpect(model().attribute("userPayAppDTO",expectedUserPayAppDTO))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "user", roles = {
            "USER"
    })
    public void should_save_updated_userPayApp_successfully() throws Exception {
        UserPayApp connectedUserPayApp = new UserPayApp(6,"erin@gmail.com", "erin",
                100,"erin BK", "user");

        UserPayAppDTO attributeUserPayAppDTO = new UserPayAppDTO("erin@gmail.com", "erin",
                "erin BK");
        UserPayAppDTO userPayAppDTO = new UserPayAppDTO("erin@gmail.com", "erin_password",
                "erin_selfAccount");
        UserPayApp userPayApp = new UserPayApp("erin@gmail.com", "erin_password",
                "erin_selfAccount");
        ObjectMapper objectMapper = new ObjectMapper();


        when(connectedUserInfo.getConnectedUserId(any(Principal.class))).thenReturn(6);
        when(userService.getUserById(6)).thenReturn(Optional.of(connectedUserPayApp));
        when(validationService.validateUserPayAppDTO(any(UserPayAppDTO.class))).thenReturn("");

        when(userPayAppMapper.toUserPayApp(userPayAppDTO)).thenReturn(userPayApp);
        mockMvc.perform(post("/updateUserPayApp").with(csrf())
                        .content(objectMapper.writeValueAsBytes(attributeUserPayAppDTO))
                        .param("email", "erin@gmail.com")
                        .param("passWord", "erin_password")
                        .param("selfAccount", "erin_selfAccount"))
                .andExpect(view().name("update-userPayApp"))
                .andExpect(model().attributeExists("userPayAppDTO"))
                .andExpect(model().attribute("userPayAppDTO",userPayAppDTO))

                .andExpect(status().is2xxSuccessful());
              verify(userService).saveUser(userPayApp);
    }



}



