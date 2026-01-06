package com.voluntree.backend.dto.registration;

    
    public record OrganizationResponse(
    String name, 
    String email,
    String phoneNumber,
    String cep,
    String companyName,
    String cause
) {} 
    
