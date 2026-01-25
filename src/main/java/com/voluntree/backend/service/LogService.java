package com.voluntree.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.voluntree.backend.domain.Log;
import com.voluntree.backend.enums.ActionType;
import com.voluntree.backend.enums.UserType;
import com.voluntree.backend.enums.Outcome;
import com.voluntree.backend.enums.Module;
import com.voluntree.backend.repository.LogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogService {

  private final LogRepository logRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveSuccessLog(String message, Long userId, Long affectedResourceId, UserType userType,
      ActionType actionType, Module module) {
    Log log = new Log.Builder().message(message)
        .userId(userId).affectedResourceId(affectedResourceId)
        .userType(userType).actionType(actionType)
        .outcome(Outcome.SUCCESS).module(module).build();

    logRepository.save(log);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveFailureLog(String message, Long userId, Long affectedResourceId, UserType userType,
      ActionType actionType, Module module) {
    Log log = new Log.Builder().message(message)
        .userId(userId).affectedResourceId(affectedResourceId)
        .userType(userType).actionType(actionType)
        .outcome(Outcome.FAIL).module(module).build();

    logRepository.save(log);
  }
}
