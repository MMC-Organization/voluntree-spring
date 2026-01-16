package com.voluntree.backend.dto.profile;

public record UpdateProfileRequest(
    String name,
    String email,
    String phoneNumber,
    String cep,
    String number
) {}

