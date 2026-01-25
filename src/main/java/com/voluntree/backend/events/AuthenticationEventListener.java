package com.voluntree.backend.events;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.voluntree.backend.domain.CustomUserDetails;
import com.voluntree.backend.domain.Log;
import com.voluntree.backend.enums.ActionType;
import com.voluntree.backend.enums.Module;
import com.voluntree.backend.enums.Outcome;
import com.voluntree.backend.repository.LogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationEventListener {

  private final LogRepository repo;

  @EventListener
  public void onSuccess(AuthenticationSuccessEvent event) {
    Authentication auth = event.getAuthentication();

    CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

    Log log = new Log("Usuário autenticado com sucesso!", user.getUserId(), null,
        user.getUserType(), ActionType.SIGNIN, Outcome.SUCCESS, Module.AUTH);

    repo.save(log);
  }

  @EventListener
  public void onFailure(AbstractAuthenticationFailureEvent event) {
    Authentication auth = event.getAuthentication();

    Log log = new Log("Erro de autenticação com email: " + auth.getName(), null, null,
        null, ActionType.SIGNIN, Outcome.FAIL, Module.AUTH);

    repo.save(log);
  }
}
