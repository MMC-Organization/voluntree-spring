package com.voluntree.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.voluntree.backend.domain.CustomUserDetails;
import com.voluntree.backend.dto.audit.AuditLogResponse;
import com.voluntree.backend.service.AuditService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/history")
    public ResponseEntity<Page<AuditLogResponse>> getMyAuditHistory(
            @PageableDefault(size = 10) Pageable pageable // O Spring tratando a paginação 
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        Long userId = user.getUserId();
//////////////////////
        Page<AuditLogResponse> logs = auditService.getUserLogs(userId, null, pageable);

        return ResponseEntity.ok(logs);
    }
}