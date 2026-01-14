package com.voluntree.backend.dto.signup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OrganizationRequest(

    @NotBlank(message = "O nome não pode ser vazio")
    String name,

    @NotBlank
    @Email(message = "E-mail inválido")
    String email,

    @Size(min = 8, max = 20, message = "A senha deve ter entre 8 e 20 caracteres")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,}$", message = "Senha deve conter letras maiúsculas, minúsculas, simbolo e números")    
    String password,

    @NotBlank
    String phoneNumber,

    @NotBlank
    String cep,

    @NotBlank
    String number,

    @NotBlank
    String cnpj,

    @NotBlank
    String companyName,

    String cause
) {}