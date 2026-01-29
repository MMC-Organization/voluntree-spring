package com.voluntree.backend.config.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    if (!(accessDeniedException instanceof InvalidCsrfTokenException)
        && !(accessDeniedException instanceof MissingCsrfTokenException)) {
      response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado!");
      return;
    }

    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write("""
          {
            "error": "XSRF_TOKEN_INVALID",
            "message": "Token XSRF não existe ou é inválido!"
          }
        """);
  }

}
