package br.com.pepper.demouser.domains.auth.controllers.dtos;

import br.com.pepper.demouser.domains.application.annotations.ValidEmail;
import br.com.pepper.demouser.domains.application.annotations.ValidPassword;
import br.com.pepper.demouser.domains.application.annotations.ValidTextInput;
import jakarta.validation.constraints.Size;

public record SignUpDto(@ValidTextInput
                        @Size(max = 255, message = "{field.length.exceeded}") String name,
                        @ValidEmail String email,
                        @ValidPassword String password){
}