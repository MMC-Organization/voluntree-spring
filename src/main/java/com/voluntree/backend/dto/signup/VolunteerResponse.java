package com.voluntree.backend.dto.signup;

import lombok.Data;
public record VolunteerResponse(
    String name, 
    String email
) {}
