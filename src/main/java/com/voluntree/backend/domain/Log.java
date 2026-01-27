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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Log {

  public Log(String message, String userIp, Long userId, Long affectedResourceId, String affectedUserEmail,
      UserType userType,
      ActionType actionType,
      Outcome outcome, Module module) {
    this.message = message;
    this.userIp = userIp;
    this.userId = userId;
    this.affectedResourceId = affectedResourceId;
    this.affectedUserEmail = affectedUserEmail;
    this.userType = userType;
    this.actionType = actionType;
    this.outcome = outcome;
    this.module = module;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(updatable = false, insertable = false, nullable = false)
  private Instant createdAt;

  @Column(nullable = false, updatable = false)
  private String message;

  @Column(nullable = false, updatable = false)
  private String userIp;

  @Column(updatable = false)
  private Long userId;

  @Column(updatable = false)
  private Long affectedResourceId;

  @Column(updatable = false)
  private String affectedUserEmail;

  @Column(updatable = false, length = 30)
  private UserType userType;

  @Column(nullable = false, updatable = false, length = 30)
  private ActionType actionType;

  @Column(nullable = false, updatable = false, length = 20)
  private Outcome outcome;

  @Column(nullable = false, updatable = false, length = 20)
  private Module module;

  public static class Builder {
    private String message;
    private String userIp;
    private Long userId;
    private Long affectedResourceId;
    private String affectedUserEmail;
    private UserType userType;
    private ActionType actionType;
    private Outcome outcome;
    private Module module;

    public Builder message(String message) {
      this.message = message;
      return this;
    }

    public Builder userIp(String userIp) {
      this.userIp = userIp;
      return this;
    }

    public Builder userId(Long userId) {
      this.userId = userId;
      return this;
    }

    public Builder affectedResourceId(Long affectedResourceId) {
      this.affectedResourceId = affectedResourceId;
      return this;
    }

    public Builder affectedUserEmail(String affectedUserEmail) {
      this.affectedUserEmail = affectedUserEmail;
      return this;
    }

    public Builder userType(UserType userType) {
      this.userType = userType;
      return this;
    }

    public Builder actionType(ActionType actionType) {
      this.actionType = actionType;
      return this;
    }

    public Builder outcome(Outcome outcome) {
      this.outcome = outcome;
      return this;
    }

    public Builder module(Module module) {
      this.module = module;
      return this;
    }

    public Log build() {
      Log log = new Log();

      log.setMessage(this.message);
      log.setUserIp(this.userIp);
      log.setUserId(this.userId);
      log.setAffectedResourceId(this.affectedResourceId);
      log.setAffectedUserEmail(this.affectedUserEmail);
      log.setUserType(this.userType);
      log.setActionType(this.actionType);
      log.setOutcome(this.outcome);
      log.setModule(this.module);

      return log;
    }
  }
}
