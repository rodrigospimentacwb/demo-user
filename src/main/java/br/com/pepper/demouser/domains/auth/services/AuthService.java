package br.com.pepper.demouser.domains.auth.services;

import br.com.pepper.demouser.domains.auth.controllers.dtos.SignInDto;
import br.com.pepper.demouser.domains.users.models.User;
import br.com.pepper.demouser.domains.users.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    public User authenticate(SignInDto input) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.email(),input.password()));
        return userRepository.findByEmail(input.email()).orElseThrow();
    }
}
