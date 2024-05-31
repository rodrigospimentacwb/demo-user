package br.com.pepper.demouser.domains.application.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NotNull(message = "{field.cannot.br.null.or.empty}")
@NotEmpty(message = "{field.cannot.br.null.or.empty}")
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface ValidTextInput {
    String message() default "Field must not be null or empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}