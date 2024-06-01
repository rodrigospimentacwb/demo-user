package br.com.pepper.demouser.unit.tests.domains.auth.services;

import br.com.pepper.demouser.commons.AbstractUnitTest;
import br.com.pepper.demouser.domains.application.handlers.CustomException;
import br.com.pepper.demouser.domains.auth.controllers.dtos.SignInDto;
import br.com.pepper.demouser.domains.auth.services.AuthService;
import br.com.pepper.demouser.domains.users.models.User;
import br.com.pepper.demouser.domains.users.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AuthServiceTest extends AbstractUnitTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testAuthenticate() {
        SignInDto input = createSignInDto();
        User user = createUser(1L);
        Authentication authentication = mock(Authentication.class);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        User authenticatedUser = authService.authenticate(createSignInDto());

        assertEquals(user, authenticatedUser);
        verify(userRepository, times(1)).findByEmail(input.email());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testAuthenticateUserNotFound() {
        SignInDto input = createSignInDto();

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class, () -> authService.authenticate(input));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatusCode());
        verify(userRepository, times(1)).findByEmail(input.email());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testAuthenticateInvalidCredentials() {
        SignInDto input = createSignInDto();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.authenticate(input));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, never()).findByEmail(input.email());
    }
}