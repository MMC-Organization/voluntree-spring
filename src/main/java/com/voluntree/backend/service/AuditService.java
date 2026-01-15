package com.voluntree.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voluntree.backend.domain.Log;
import com.voluntree.backend.dto.audit.AuditLogResponse;
import com.voluntree.backend.mapper.AuditMapper;
import com.voluntree.backend.repository.LogRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuditService {

    // Aqui virá o AuditRepository depois (Parte da Mariana)
    private final LogRepository logRepository;
    private final AuditMapper auditMapper; // Injetamos o mapper da Clara

    @Transactional(readOnly = true)
    public Page<AuditLogResponse> getUserLogs(Long userId, Pageable pageable) {
        // 1. Busca os logs no banco
        Page<Log> logs = logRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);

        // 2. Converte usando o Mapper oficial
        // O ::toResponse chama a função que você me mandou agora
        return logs.map(auditMapper::toResponse);
    }
}