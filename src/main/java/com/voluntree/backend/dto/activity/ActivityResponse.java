package com.voluntree.backend.dto.activity;

import java.time.LocalDateTime;

public record ActivityResponse(
    Long id,
    String name,
    String description,
    Short spots,
    String cep,
    String number,
    LocalDateTime activityDate,
    Long organizationId,
    String organizationName,
    String organizationCompanyName,
    Boolean canceled
) {}
