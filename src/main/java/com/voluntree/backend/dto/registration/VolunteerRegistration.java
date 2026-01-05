package com.voluntree.backend.dto.registration;


public record VolunteerRegistration(
    String name,
    String email,
    String password,
    String phoneNumber,
    String cep,
    String number,
    String cpf
) {}