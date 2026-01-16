package com.voluntree.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voluntree.backend.domain.Log;
import com.voluntree.backend.dto.audit.AuditLogResponse;
import com.voluntree.backend.enums.UserType;
import com.voluntree.backend.mapper.AuditMapper;
import com.voluntree.backend.repository.LogRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuditService {

    // Aqui virá o AuditRepository depois (Parte da Mariana)
    private final LogRepository logRepository;
    private final AuditMapper auditMapper; 

    @Transactional(readOnly = true)
    public Page<AuditLogResponse> getUserLogs(Long userId, UserType userType, Pageable pageable) {
        
        
        Page<Log> logs = logRepository.findByUserIdAndUserTypeOrderByCreatedAtDesc(userId, userType, pageable);

        return logs.map(auditMapper::toResponse);
    }
}