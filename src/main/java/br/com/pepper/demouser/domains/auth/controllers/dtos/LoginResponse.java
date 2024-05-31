package br.com.pepper.demouser.domains.auth.controllers.dtos;

public record LoginResponse(String token, Long expiresIn) {
}
