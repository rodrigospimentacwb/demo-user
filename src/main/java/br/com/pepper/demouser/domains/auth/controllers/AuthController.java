package br.com.pepper.demouser.domains.auth.controllers;

import br.com.pepper.demouser.domains.auth.controllers.dtos.LoginResponse;
import br.com.pepper.demouser.domains.auth.controllers.dtos.SignInDto;
import br.com.pepper.demouser.domains.auth.controllers.dtos.SignUpDto;
import br.com.pepper.demouser.domains.auth.services.AuthService;
import br.com.pepper.demouser.domains.auth.services.JWTService;
import br.com.pepper.demouser.domains.users.controllers.dtos.UserDto;
import br.com.pepper.demouser.domains.users.models.User;
import br.com.pepper.demouser.domains.users.services.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthController {
    @Resource
    private JWTService jwtService;

    @Resource
    private AuthService authenticationService;

    @Resource
    private UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody SignInDto signInDto) {
        User authenticatedUser = authenticationService.authenticate(signInDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        return ResponseEntity.ok(new LoginResponse(jwtToken, jwtService.getExpirationTime()));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> addUser(@RequestBody SignUpDto signUpDto) {
        User registeredUser = userService.addUserAndNotify(signUpDto);
        return ResponseEntity.ok(userService.convertToDto(registeredUser));
    }
}
