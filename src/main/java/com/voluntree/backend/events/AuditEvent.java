package com.voluntree.backend.events;

import com.voluntree.backend.enums.ActionType;
import com.voluntree.backend.enums.Module;
import com.voluntree.backend.enums.Outcome;
import com.voluntree.backend.enums.UserType;

public record AuditEvent(String message, Long userId, UserType userType, ActionType actionType, Outcome outcome,
    Module module) {
}
