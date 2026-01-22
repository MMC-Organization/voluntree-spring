package com.voluntree.backend.dto;

import java.time.Instant;

public record RegistrationDTO(
    Long id,
    Long activityId,
    String activityName,
    String volunteerName,
    Instant registeredAt,
    Boolean canceled
) {
}
