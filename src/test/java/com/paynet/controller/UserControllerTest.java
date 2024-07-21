package com.paynet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paynet.model.UserPayApp;
import com.paynet.service.UserService;
import com.paynet.service.ValidationService;
import com.paynet.web.dto.UserPayAppDTO;
import com.paynet.web.mapper.UserPayAppMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
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
    @MockBean
    private ValidationService validationService;
    @MockBean
    private TransactionController transactionController;


    @Test
    @WithMockUser(username = "user")
    public void should_redirect_to_add_userPayApp_page_without_prepopulated_email() throws Exception {
        //given
        UserPayAppDTO expectedUserPayAppDTO = new UserPayAppDTO();
        when(connectedUserInfo.isInstanceOfOAuth2AuthenticationToken(any(Principal.class))).thenReturn(false);
        //when
        mockMvc.perform(get("/inscription"))
                //then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("add-userPayApp"))
                .andExpect(model().attributeExists("userPayAppDTO"))
                .andExpect(model().attribute("userPayAppDTO", expectedUserPayAppDTO));
    }

    @Test
    @WithMockUser(username = "user")
    public void should_redirect_to_add_userPayApp_page_with_prepopulated_email() throws Exception {
        //given
        UserPayAppDTO expectedUserPayAppDTO = new UserPayAppDTO();
        expectedUserPayAppDTO.setEmail("erin@gmail.com");
        when(connectedUserInfo.isInstanceOfOAuth2AuthenticationToken(any(Principal.class))).thenReturn(true);
        when(connectedUserInfo.getConnectedUserEmail(any(Principal.class))).thenReturn("erin@gmail.com");
        //when
        mockMvc.perform(get("/inscription"))
                //then
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("add-userPayApp"))
                .andExpect(model().attributeExists("userPayAppDTO"))
                .andExpect(model().attribute("userPayAppDTO", expectedUserPayAppDTO));
    }

    @Test
    @WithMockUser(username = "user")
    public void should_save_valid_userPayApp_successfully() throws Exception {
        //given
        when(validationService.validateUserPayAppDTO(any(UserPayAppDTO.class))).thenReturn("");
        UserPayApp userPayApp = new UserPayApp("erin@gmail.com", "erin_password",
                "erin_selfAccount");
        when(userPayAppMapper.toUserPayApp(any(UserPayAppDTO.class))).thenReturn(userPayApp);
        UserPayAppDTO userPayAppDTO = new UserPayAppDTO("", "", "");
        ObjectMapper objectMapper = new ObjectMapper();
        //when
        mockMvc.perform(post("/addUserPayApp").with(csrf())
                        .content(objectMapper.writeValueAsBytes(userPayAppDTO))
                        .param("email", "erin@gmail.com")
                        .param("passWord", "erin_password")
                        .param("selfAccount", "erin_selfAccount"))
                //then
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/showLoginPage"));
        verify(userService).saveUser(userPayApp);
    }

    @Test
    @WithMockUser(username = "user")
    public void should_return_to_add_userPayApp_page_without_saving_new_userPayApp() throws Exception {
        //given
        when(validationService.validateUserPayAppDTO(any(UserPayAppDTO.class))).
                thenReturn("\n Votre mail doit être renseigné." + "\n Votre mot de passe doit être renseigné."
                        + "\n Le compte bancaire associé doit être renseigné.");
        UserPayAppDTO userPayAppDTO = new UserPayAppDTO("", "", "");
        ObjectMapper objectMapper = new ObjectMapper();
        //when
        mockMvc.perform(post("/addUserPayApp").with(csrf())
                        .content(objectMapper.writeValueAsBytes(userPayAppDTO))
                        .param("email", "")
                        .param("passWord", "")
                        .param("selfAccount", ""))
                //then
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("add-userPayApp"))
                .andExpect(model().attributeExists("userPayAppDTO"))
                .andExpect(model().attribute("userPayAppDTO", userPayAppDTO));
        verify(userService, Mockito.times(0)).saveUser(any(UserPayApp.class));
    }

    @Test
    @WithMockUser(username = "user")
    public void should_redirect_to_update_userPayApp_page() throws Exception {
        // given
        UserPayApp userPayApp = new UserPayApp("erin@gmail.com", "erin",
                "erin bank");
        UserPayAppDTO expectedUserPayAppDTO = new UserPayAppDTO("erin@gmail.com", "",
                "erin bank");
        when(connectedUserInfo.getConnectedUserId(any(Principal.class))).thenReturn(6);
        when(userService.getUserById(6)).thenReturn(userPayApp);
        when(userPayAppMapper.toUserPayAppDTO(userPayApp)).thenReturn(expectedUserPayAppDTO);
        when(userService.getEmailById(6)).thenReturn("erin@gmail.com");
        // when
        mockMvc.perform(get("/editUserPayApp"))
                // then
                .andDo(print())
                .andExpect(view().name("update-userPayApp"))
                .andExpect(model().attributeExists("userPayAppDTO"))
                .andExpect(model().attribute("userPayAppDTO", expectedUserPayAppDTO))
                .andExpect(model().attributeExists("connectedUserEmail"))
                .andExpect(model().attribute("connectedUserEmail", "erin@gmail.com"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "user")
    public void should_save_updated_userPayApp_successfully() throws Exception {
        //given
        UserPayApp connectedUserPayApp = new UserPayApp(6, "erin@gmail.com", "erin",
                100, "erin BK", "user");
        UserPayAppDTO attributeUserPayAppDTO = new UserPayAppDTO("erin@gmail.com", "",
                "erin BK");
        UserPayAppDTO userPayAppDTO = new UserPayAppDTO("erin@gmail.com", "erin_password",
                "erin_selfAccount");
        UserPayAppDTO expectedUserPayAppDTO = new UserPayAppDTO("erin@gmail.com", "",
                "erin_selfAccount");
        UserPayApp userPayApp = new UserPayApp("erin@gmail.com", "crypted_erin_password",
                "erin_selfAccount");
        ObjectMapper objectMapper = new ObjectMapper();
        when(connectedUserInfo.getConnectedUserId(any(Principal.class))).thenReturn(6);
        when(userService.getUserById(6)).thenReturn(connectedUserPayApp);
        when(validationService.validateUserPayAppDTO(any(UserPayAppDTO.class))).thenReturn("");
        when(userPayAppMapper.toUserPayApp(userPayAppDTO)).thenReturn(userPayApp);
        when(userService.getEmailById(6)).thenReturn("erin@gmail.com");
        //when
        mockMvc.perform(post("/updateUserPayApp").with(csrf())
                        .content(objectMapper.writeValueAsBytes(attributeUserPayAppDTO))
                        .param("email", "erin@gmail.com")
                        .param("passWord", "erin_password")
                        .param("selfAccount", "erin_selfAccount"))
                //then
                .andDo(print())
                .andExpect(view().name("update-userPayApp"))
                .andExpect(model().attributeExists("userPayAppDTO"))
                .andExpect(model().attribute("userPayAppDTO", expectedUserPayAppDTO))
                .andExpect(model().attributeExists("connectedUserEmail"))
                .andExpect(model().attribute("connectedUserEmail", "erin@gmail.com"))
                .andExpect(status().is2xxSuccessful());
        verify(userService).saveUser(userPayApp);
    }

    @Test
    @WithMockUser(username = "user")
    public void should_redirect_to_update_userPayApp_page_without_saving_any_modification() throws Exception {
        //given
        UserPayApp connectedUserPayApp = new UserPayApp(6, "erin@gmail.com", "erin",
                100, "erin BK", "user");
        UserPayAppDTO attributeUserPayAppDTO = new UserPayAppDTO("erin@gmail.com", "",
                "erin BK");
        when(connectedUserInfo.getConnectedUserId(any(Principal.class))).thenReturn(6);
        when(userService.getUserById(6)).thenReturn(connectedUserPayApp);
        when(validationService.validateUserPayAppDTO(any(UserPayAppDTO.class))).thenReturn(
                "\n Votre mail doit être renseigné."
                        + "\n Votre mot de passe doit être renseigné."
                        + "\n Le compte bancaire associé doit être renseigné.");
        when(userService.getEmailById(6)).thenReturn("erin@gmail.com");
        ObjectMapper objectMapper = new ObjectMapper();
        //when
        mockMvc.perform(post("/updateUserPayApp").with(csrf())
                        .content(objectMapper.writeValueAsBytes(attributeUserPayAppDTO))
                        .param("email", "")
                        .param("passWord", "")
                        .param("selfAccount", ""))
                //then
                .andDo(print())
                .andExpect(view().name("update-userPayApp"))
                .andExpect(model().attributeExists("userPayAppDTO"))
                .andExpect(model().attribute("userPayAppDTO", attributeUserPayAppDTO))
                .andExpect(model().attributeExists("connectedUserEmail"))
                .andExpect(model().attribute("connectedUserEmail", "erin@gmail.com"))
                .andExpect(status().is2xxSuccessful());
        verify(userService, Mockito.times(0)).saveUser(any(UserPayApp.class));
    }

}
