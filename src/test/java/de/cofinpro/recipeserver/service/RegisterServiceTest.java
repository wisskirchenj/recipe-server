package de.cofinpro.recipeserver.service;

import de.cofinpro.recipeserver.entities.User;
import de.cofinpro.recipeserver.repository.UserRepository;
import de.cofinpro.recipeserver.service.impl.RegisterServiceImpl;
import de.cofinpro.recipeserver.service.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @Mock
    UserRepository userRepository;

    RegisterService service;

    @BeforeEach
    void setup() {
        service = new RegisterServiceImpl(userRepository);
    }

    @Test
    void whenUserExist_RegisterUserThrows() {
        when(userRepository.existsByUsername("test")).thenReturn(true);
        var newUser = new User().setUsername("test");
        assertThrows(UserAlreadyExistsException.class, () -> service.registerUser(newUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void whenRegisterUser_UserSaved() {
        when(userRepository.existsByUsername("test")).thenReturn(false);
        var newUser = new User().setUsername("test");
        service.registerUser(newUser);
        verify(userRepository).save(newUser);
    }
}