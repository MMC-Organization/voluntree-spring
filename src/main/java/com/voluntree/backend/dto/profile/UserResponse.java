package com.voluntree.backend.dto.profile;

public record UserResponse(
    Long id,
    String name,
    String email,
    String phoneNumber,
    String cep
) {}