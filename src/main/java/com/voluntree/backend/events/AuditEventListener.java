package com.voluntree.backend.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

import com.voluntree.backend.domain.Log;
import com.voluntree.backend.repository.LogRepository;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuditEventListener {

  private final LogRepository repo;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @EventListener
  public void onAuditEvent(AuditEvent event) {
    Log log = new Log(null, null, event.message(), event.userId(), event.affectedResourceId(), event.userType(),
        event.actionType(),
        event.outcome(), event.module());

    repo.save(log);
  }
}
