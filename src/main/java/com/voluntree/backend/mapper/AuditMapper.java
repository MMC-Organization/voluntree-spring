package com.voluntree.backend.mapper;

import java.time.ZoneId;

import org.springframework.stereotype.Component;

import com.voluntree.backend.domain.Log;
import com.voluntree.backend.dto.audit.AuditLogResponse;

@Component
public class AuditMapper {

    public AuditLogResponse toResponse(Log entity) {
        if (entity == null) {
            return null;
        }

        return new AuditLogResponse(
            entity.getId(),
            entity.getActionType().toString(), 
            entity.getModule().toString(),    
            entity.getMessage(),               
            
           
            entity.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime()
        );
    }
}