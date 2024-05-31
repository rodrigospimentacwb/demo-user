package br.com.pepper.demouser.domains.users.controllers.dtos;

import jakarta.validation.constraints.NotNull;

public record ListUserDto(String name, Integer page, Integer size) {
}
