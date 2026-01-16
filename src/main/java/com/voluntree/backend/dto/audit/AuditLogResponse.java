package com.voluntree.backend.dto.audit;

import java.time.LocalDateTime;

public record AuditLogResponse(
    Long id,
    String acao,       
    String modulo,     // a area que foi alterada"
    String descricao,  
    LocalDateTime timestamp
) {}