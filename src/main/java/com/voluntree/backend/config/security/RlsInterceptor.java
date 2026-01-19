package com.voluntree.backend.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class RlsInterceptor extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    try {
      // Captura o usuário autenticado do SecurityContext e armazena no ThreadLocal
      RlsContext.setFromSecurityContext();
      
      filterChain.doFilter(request, response);
    } finally {
      // Limpa o contexto ao final da requisição para evitar memory leaks
      RlsContext.clear();
    }
  }
}
