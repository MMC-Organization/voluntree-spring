package com.voluntree.backend.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.voluntree.backend.domain.volunteer.Volunteer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = { "volunteer_id", "activity_id" })
})
public class Registration {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "volunteer_id")
  private Volunteer volunteer;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "activity_id")
  private Activity activity;

  @CreationTimestamp
  @Column(updatable = false, insertable = false)
  LocalDateTime registeredAt;
}
