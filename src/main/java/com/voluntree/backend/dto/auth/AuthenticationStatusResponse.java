package com.voluntree.backend.dto.auth;

import com.voluntree.backend.enums.UserType;

public record AuthenticationStatusResponse(String message, Boolean status, UserType userType) {

}
