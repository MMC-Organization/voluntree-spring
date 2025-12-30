package com.voluntree.backend.domain;

import java.time.Instant;

import com.voluntree.backend.enums.ActionType;
import com.voluntree.backend.enums.Module;
import com.voluntree.backend.enums.Outcome;
import com.voluntree.backend.enums.UserType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;

@Entity
@AllArgsConstructor
public class Log {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(updatable = false, insertable = false,  nullable = false)
  private Instant createdAt;

  @Column(nullable = false, updatable = false)
  private String message;

  @Column(nullable = false, updatable = false)
  private Long userId;

  @Column(nullable = false, updatable = false, length = 30)
  private UserType userType;

  @Column(nullable = false, updatable = false, length = 30)
  private ActionType actionType;

  @Column(nullable = false, updatable = false, length = 20)
  private Outcome outcome;

  @Column(nullable = false, updatable = false, length = 20)
  private Module module;
}
