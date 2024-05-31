package br.com.pepper.demouser.domains.application.handlers;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final HttpStatusCode httpStatusCode;
    private Exception originalException = null;

    public CustomException(String message, HttpStatusCode httpStatusCode, Exception originalException) {
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.originalException = originalException;
    }

    public CustomException(String message, HttpStatusCode httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }
}
