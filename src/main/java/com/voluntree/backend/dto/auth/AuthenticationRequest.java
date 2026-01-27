package com.voluntree.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticationRequest(
    @Size(max = 255) @NotBlank @Email(message = "E-mail inválido") String email,
    @Size(min = 8, max = 20) String password) {

}
