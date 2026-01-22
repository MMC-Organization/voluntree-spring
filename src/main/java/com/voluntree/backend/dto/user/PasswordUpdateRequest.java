package com.voluntree.backend.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordUpdateRequest(
    @NotBlank(message = "A senha atual é obrigatória")
    String oldPassword,

    @NotBlank(message = "A nova senha não pode estar vazia")
    @Size(min = 8, max = 20, message = "A nova senha deve ter entre 8 e 20 caracteres")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,}$", 
             message = "A nova senha deve conter letras maiúsculas, minúsculas, símbolos e números")
    String newPassword
) {}
