package com.voluntree.backend.events;

import org.springframework.stereotype.Component;

import com.voluntree.backend.domain.Log;
import com.voluntree.backend.repository.LogRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuditEventListener {

  private final LogRepository repo;

  public void onAuditEvent(AuditEvent event) {
    Log log = new Log(null, null, event.message(), event.userId(), event.userType(), event.actionType(),
        event.module());

    repo.save(log);
  }
}
