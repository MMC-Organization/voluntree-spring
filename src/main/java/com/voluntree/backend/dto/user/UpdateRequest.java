
package com.voluntree.backend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateRequest(
    
    @NotBlank(message = "O nome não pode estar vazio")
    String name,

    @NotBlank
    @Size(min = 10, max = 15) 
    String phoneNumber,

    @NotBlank
    @Size(min = 8, max = 9)
    String cep,

    @NotBlank
    String number,

    @NotBlank
    @Email(message = "O e-mail deve ser válido")
    String email
    
) {}

