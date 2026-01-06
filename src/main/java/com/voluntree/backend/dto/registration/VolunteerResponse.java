package com.voluntree.backend.dto.registration;

import lombok.Data;
public record VolunteerResponse(
    String name, 
    String email
) {}
