package br.com.pepper.demouser.domains.auth.controllers.dtos;

import br.com.pepper.demouser.domains.application.annotations.ValidEmail;
import br.com.pepper.demouser.domains.application.annotations.ValidPassword;

public record SignInDto(@ValidEmail String email, @ValidPassword String password){
}