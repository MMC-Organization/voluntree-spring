package com.voluntree.backend.dto.registration;


public record OrganizationRegistration(
    String name, 
    String email,
    String password,
    String phoneNumber,
    String cep,
    String number,
    String cnpj,
    String companyName,
    String cause
) {}