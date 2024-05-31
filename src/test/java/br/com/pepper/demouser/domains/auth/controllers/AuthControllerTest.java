package br.com.pepper.demouser.domains.auth.controllers;

import br.com.pepper.demouser.domains.auth.controllers.dtos.LoginResponse;
import br.com.pepper.demouser.domains.auth.controllers.dtos.SignInDto;
import br.com.pepper.demouser.domains.auth.controllers.dtos.SignUpDto;
import br.com.pepper.demouser.domains.auth.services.AuthService;
import br.com.pepper.demouser.domains.auth.services.JWTService;
import br.com.pepper.demouser.domains.users.controllers.dtos.UserDto;
import br.com.pepper.demouser.domains.users.models.User;
import br.com.pepper.demouser.domains.users.services.UserService;
import br.com.pepper.demouser.test.commons.AbstractTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest extends AbstractTestUtils {
    @Mock
    private JWTService jwtService;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private static final String MOCKED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMUBleGFtcGxlLmNvbSIsImlhdCI6MTcxNzA3NzgxNywiZXhwIjoxNzE3MDgxNDE3fQ.yAkw495GS3vSNWhX8K1xN6c56CPEWDDmrfncGdSFGU0";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAuthenticate() {
        SignInDto signInDto = new SignInDto("jonny.silver@gmail.com", "Aa12345@");
        User authenticatedUser = new User("Johnny Silverhand", "jonny.silver@gmail.com", "Aa12345@");
        when(authService.authenticate(signInDto)).thenReturn(authenticatedUser);
        when(jwtService.generateToken(authenticatedUser)).thenReturn(MOCKED_TOKEN);

        ResponseEntity<LoginResponse> responseEntity = authController.authenticate(signInDto);

        assertEquals(MOCKED_TOKEN, Objects.requireNonNull(responseEntity.getBody()).token());

        verify(authService, times(1)).authenticate(signInDto);
        verify(jwtService, times(1)).generateToken(authenticatedUser);
    }

    @Test
    void testAddUser() {
        SignUpDto signUpDto = new SignUpDto("Johnny Silverhand", "jonny.silver@gmail.com", "Aa12345@");
        User registeredUser = new User("Johnny Silverhand", "jonny.silver@gmail.com", "Aa12345@");
        forceValue(registeredUser, "id", 1L);

        when(userService.addUserAndNotify(signUpDto)).thenReturn(registeredUser);
        when(userService.convertToDto(registeredUser)).thenReturn(new UserDto(1L, "Johnny Silverhand", "jonny.silver@gmail.com"));

        ResponseEntity<UserDto> responseEntity = authController.addUser(signUpDto);

        assertEquals(registeredUser.getId(), Objects.requireNonNull(responseEntity.getBody()).id());

        verify(userService, times(1)).addUserAndNotify(signUpDto);
        verify(userService, times(1)).convertToDto(registeredUser);
    }
}