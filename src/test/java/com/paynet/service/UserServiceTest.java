package com.paynet.service;

import com.paynet.model.UserPayApp;
import com.paynet.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    public void should_throw_an_exception_when_getting_userPayApp_corresponding_to_id_is_not_found() {
        //given
        Optional<UserPayApp> emptyUserPayApp = Optional.empty();
        when(userRepository.findById(55)).thenReturn(emptyUserPayApp);
        //when then
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> {
                    userService.getUserById(55);
                }, "IllegalArgumentException was expected");
        assertEquals("Invalid user id", thrown.getMessage());
    }

    @Test
    public void should_throw_an_exception_when_getting_userPayApp_corresponding_to_email_is_not_found() {
        //given
        Optional<UserPayApp> emptyUserPayApp = Optional.empty();
        when(userRepository.findByEmail("anton@mail.fr")).thenReturn(emptyUserPayApp);
        //when then
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> {
                    userService.getUserByEmail("anton@mail.fr");
                }, "IllegalArgumentException was expected");
        assertEquals("Invalid user email", thrown.getMessage());
    }

}
