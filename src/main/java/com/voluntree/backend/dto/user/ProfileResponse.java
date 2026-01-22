package com.voluntree.backend.dto.user;

public record ProfileResponse(
    String name,
    String email,
    String phoneNumber,
    String cep,
    String number,
    String userType,
    // Específicos de Voluntário
    String cpf,
    // Específicos de Organização
    String cnpj,
    String companyName,
    String cause
) {}