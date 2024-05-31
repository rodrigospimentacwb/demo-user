package br.com.pepper.demouser.domains.application.handlers;

import java.time.LocalDateTime;

public record ErrorMessage(String message, int status, LocalDateTime timestamp) {}
