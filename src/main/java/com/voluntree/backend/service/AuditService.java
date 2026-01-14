package com.voluntree.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.voluntree.backend.dto.audit.AuditLogResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditService {

    // Aqui virá o AuditRepository depois (Parte da Mariana)

    public Page<AuditLogResponse> getUserLogs(Long userId, Pageable pageable) {
        // Mariana vai implementa a busca no banco e a conversão aqui.
                    return null;
    }
}