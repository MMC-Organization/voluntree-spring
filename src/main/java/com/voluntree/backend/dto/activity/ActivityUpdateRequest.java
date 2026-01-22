package com.voluntree.backend.dto.activity;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ActivityUpdateRequest(

    @Size(max = 200, message = "O nome deve ter no máximo 200 caracteres")
    String name,

    @Size(max = 5000, message = "A descrição deve ter no máximo 5000 caracteres")
    String description,

    @Positive(message = "O número de vagas deve ser positivo")
    Short spots,

    @Pattern(regexp = "^[0-9]{8}$", message = "CEP deve conter 8 dígitos numéricos")
    String cep,

    @Size(max = 10, message = "O número deve ter no máximo 10 caracteres")
    String number,

    @Future(message = "A data da atividade deve ser no futuro")
    LocalDateTime activityDate
) {}
