package com.voluntree.backend.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.voluntree.backend.enums.ActionType;
import com.voluntree.backend.enums.Module;
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

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false, updatable = false)
  private String message;

  @Column(nullable = false, updatable = false)
  private Long userId;

  @Column(nullable = false, updatable = false)
  private UserType userType;

  @Column(nullable = false, updatable = false)
  private ActionType actionType;

  @Column(nullable = false, updatable = false)
  private Module module;
}
