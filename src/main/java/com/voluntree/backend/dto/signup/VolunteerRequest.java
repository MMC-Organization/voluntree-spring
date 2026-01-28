package com.voluntree.backend.dto.signup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VolunteerRequest(

    @Size(max = 150)
    @NotBlank(message = "O nome não pode ser vazio")
    String name,

    @Size(max = 255)
    @NotBlank
    @Email(message = "E-mail inválido")
    String email,

    @Size(min = 8, max = 20, message = "A senha deve ter entre 8 e 20 caracteres")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,}$", message = "Senha deve conter letras maiúsculas, minúsculas,simbolo e números")    
    String password,

    @Size(max = 25)
    @NotBlank
    String phoneNumber,

    @Size(min = 8, max = 8)
    @NotBlank
    String cep,

    @Size(max = 10)
    @NotBlank
    String number,

    @Size(max = 11)
    @NotBlank
    @Pattern(regexp = "^\\d{11}$", message = "CPF deve conter apenas números")
    String cpf
) {}