package br.com.pepper.demouser.domains.application.handlers;

import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AuthGlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(AuthGlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception) {
        ProblemDetail errorDetail = null;

        errorDetail = handleFilter(exception, errorDetail);

        if (errorDetail == null) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), exception.getMessage());
            errorDetail.setProperty("description", "Unknown internal server error.");
        }

        // TODO PEPPER Not show internal error details to user
        errorDetail.setDetail(null);

        return errorDetail;
    }

    private ProblemDetail handleFilter(Exception exception, ProblemDetail errorDetail) {
        if (exception instanceof CustomException customException) {
            errorDetail = handleCustomException((CustomException) exception, customException);
        } else {
            // TODO PEPPER Send stack to elastic Kibana/Rollbar
            logger.error("AuthGlobalExceptionHandler", exception);
        }

        if (exception instanceof BadCredentialsException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
            errorDetail.setProperty("description", "The username or password is incorrect");
        }

        if (exception instanceof AccountStatusException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty("description", "The account is locked");
        }

        if (exception instanceof AccessDeniedException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty("description", "You are not authorized to access this resource");
        }

        if (exception instanceof SignatureException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty("description", "The JWT signature is invalid");
        }

        if (exception instanceof ExpiredJwtException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty("description", "The JWT token has expired");
        }

        if (exception instanceof HttpMessageNotReadableException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());
            errorDetail.setProperty("description", "Bad Request");
        }

        if (exception instanceof MethodArgumentNotValidException) {
            errorDetail = handleMethodArgumentNotValidException((MethodArgumentNotValidException) exception);
        }

        if (exception instanceof HttpRequestMethodNotSupportedException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());
            errorDetail.setProperty("description", "Bad Request");
        }
        return errorDetail;
    }

    private static ProblemDetail handleCustomException(CustomException exception, CustomException customException) {
        ProblemDetail errorDetail;
        errorDetail = ProblemDetail.forStatusAndDetail(customException.getHttpStatusCode(), customException.getMessage());
        errorDetail.setProperty("description", customException.getMessage());
        errorDetail.setProperty("timestamp", LocalDateTime.now().toString());

        if (exception.getOriginalException() != null) {
            // TODO PEPPER Send stack to elastic Kibana/Rollbar
            logger.error("AuthGlobalExceptionHandler", exception.getOriginalException());
        }
        return errorDetail;
    }

    private ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), "Validation failed");
        errorDetail.setProperty("fieldErrors", fieldErrors);
        errorDetail.setProperty("description", "One or more fields have an error");
        return errorDetail;
    }
}
