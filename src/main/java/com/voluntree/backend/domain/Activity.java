package com.voluntree.backend.domain;

import java.time.LocalDateTime;

import com.voluntree.backend.domain.organization.Organization;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Activity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 200)
  private String name;

  @Column
  private String description;

  @Column
  private Short spots;

  @Column(nullable = false, length = 8)
  private String cep;

  @Column(length = 10)
  private String number;

  @Column(nullable = false)
  private LocalDateTime activityDate;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "organization_id", nullable = false)
  private Organization organization;

  @Column
  private Boolean canceled;
}
