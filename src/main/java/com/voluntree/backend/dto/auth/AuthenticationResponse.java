package com.voluntree.backend.dto.auth;

public record AuthenticationResponse(
    Long id,
    String token,   
    String name,      
    String userType
) {}
