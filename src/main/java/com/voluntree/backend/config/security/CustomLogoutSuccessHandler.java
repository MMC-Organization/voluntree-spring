package com.voluntree.backend.config.security;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.voluntree.backend.domain.CustomUserDetails;
import com.voluntree.backend.domain.Log;
import com.voluntree.backend.enums.ActionType;
import com.voluntree.backend.enums.Module;
import com.voluntree.backend.enums.Outcome;
import com.voluntree.backend.repository.LogRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
  private final LogRepository repo;

  @Override
  public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse res, @Nullable Authentication auth) {
    if (auth != null && auth.getPrincipal() instanceof CustomUserDetails user) {
      repo.save(new Log("Usuário desconectado!", user.getUserId(), null, user.getUserType(),
          ActionType.SIGNOUT, Outcome.SUCCESS, Module.AUTH));
    }

    res.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

}
