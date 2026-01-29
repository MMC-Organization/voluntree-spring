package com.voluntree.backend.dto.auth;

public record AuthenticationResponse(Boolean authenticated, String message) {
  
}
