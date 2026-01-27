package com.voluntree.backend.dto.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
    
    @Size(max = 150)
    @NotEmpty(message = "O nome não pode ser vazio")
    String name,

    @Size(max = 255)
    @NotEmpty
    @Email(message = "E-mail inválido")
    String email,
    
    @Size(max = 25)
    @NotEmpty
    String phoneNumber,

    @Size(min = 8, max = 8)
    @NotEmpty
    String cep,

    @Size(max = 10)
    @NotEmpty
    String number
) {}

