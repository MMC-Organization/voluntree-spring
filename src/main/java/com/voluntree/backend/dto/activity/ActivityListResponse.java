package com.voluntree.backend.dto.activity;

import java.time.LocalDateTime;

public record ActivityListResponse(
    Long id,
    String name,
    String description,
    Short spots,
    String cep,
    LocalDateTime activityDate,
    String organizationName,
    Boolean canceled
) {}
