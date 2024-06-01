package br.com.pepper.demouser.unit.tests.domains.auth.controllers;

import br.com.pepper.demouser.domains.auth.controllers.AuthController;
import br.com.pepper.demouser.domains.auth.controllers.dtos.LoginResponse;
import br.com.pepper.demouser.domains.auth.controllers.dtos.SignInDto;
import br.com.pepper.demouser.domains.auth.controllers.dtos.SignUpDto;
import br.com.pepper.demouser.domains.auth.services.AuthService;
import br.com.pepper.demouser.domains.auth.services.JWTService;
import br.com.pepper.demouser.domains.users.controllers.dtos.UserDto;
import br.com.pepper.demouser.domains.users.models.User;
import br.com.pepper.demouser.domains.UserService;
import br.com.pepper.demouser.commons.AbstractUnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest extends AbstractUnitTest {
    @Mock
    private JWTService jwtService;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testAuthenticate() {
        SignInDto signInDto = createSignInDto();
        User authenticatedUser = createUser(1L);

        when(authService.authenticate(signInDto)).thenReturn(authenticatedUser);
        when(jwtService.generateToken(authenticatedUser)).thenReturn(MOCKED_TOKEN);

        ResponseEntity<LoginResponse> responseEntity = authController.authenticate(signInDto);

        assertEquals(MOCKED_TOKEN, Objects.requireNonNull(responseEntity.getBody()).token());

        verify(authService, times(1)).authenticate(signInDto);
        verify(jwtService, times(1)).generateToken(authenticatedUser);
    }

    @Test
    void testAddUser() {
        SignUpDto signUpDto = createSignUpDto();
        User registeredUser = createUser(1L);

        when(userService.addUserAndNotify(signUpDto)).thenReturn(registeredUser);
        when(userService.convertToDto(registeredUser)).thenReturn(createUserDto(1L));

        ResponseEntity<UserDto> responseEntity = authController.addUser(signUpDto);

        assertEquals(registeredUser.getId(), Objects.requireNonNull(responseEntity.getBody()).id());

        verify(userService, times(1)).addUserAndNotify(signUpDto);
        verify(userService, times(1)).convertToDto(registeredUser);
    }
}