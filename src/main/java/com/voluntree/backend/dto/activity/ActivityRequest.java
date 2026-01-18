package com.voluntree.backend.dto.activity;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ActivityRequest(

    @NotBlank(message = "O nome da atividade não pode ser vazio")
    @Size(max = 200, message = "O nome deve ter no máximo 200 caracteres")
    String name,

    @Size(max = 5000, message = "A descrição deve ter no máximo 5000 caracteres")
    String description,

    @Positive(message = "O número de vagas deve ser positivo")
    Short spots,

    @NotBlank(message = "O CEP não pode ser vazio")
    @Pattern(regexp = "^[0-9]{8}$", message = "CEP deve conter 8 dígitos numéricos")
    String cep,

    @Size(max = 10, message = "O número deve ter no máximo 10 caracteres")
    String number,

    @NotNull(message = "A data da atividade não pode ser nula")
    @Future(message = "A data da atividade deve ser no futuro")
    LocalDateTime activityDate
) {}
