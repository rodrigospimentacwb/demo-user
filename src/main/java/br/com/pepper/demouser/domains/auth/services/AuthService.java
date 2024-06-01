package br.com.pepper.demouser.domains.auth.services;

import br.com.pepper.demouser.domains.application.handlers.CustomException;
import br.com.pepper.demouser.domains.auth.controllers.dtos.SignInDto;
import br.com.pepper.demouser.domains.users.models.User;
import br.com.pepper.demouser.domains.users.repositories.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private AuthenticationManager authenticationManager;

    public User authenticate(SignInDto input) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.email(),input.password()));
        return userRepository.findByEmail(input.email())
                .orElseThrow(() -> new CustomException("Invalid username or password", HttpStatus.BAD_REQUEST));
    }
}
