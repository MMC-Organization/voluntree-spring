package com.voluntree.backend.config.security;

import com.voluntree.backend.domain.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utilitário para armazenar e recuperar o ID do usuário autenticado no contexto da thread.
 * Usado pelo RlsAspect para configurar as políticas RLS no PostgreSQL.
 */
public class RlsContext {
  
  private static final ThreadLocal<Long> currentUserId = new ThreadLocal<>();

  public static void setCurrentUserId(Long userId) {
    currentUserId.set(userId);
  }

  
  public static void setFromSecurityContext() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
    if (authentication != null 
        && authentication.isAuthenticated() 
        && !"anonymousUser".equals(authentication.getPrincipal())
        && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
      currentUserId.set(userDetails.getUserId());
    }
  }

  public static Long getCurrentUserId() {
    return currentUserId.get();
  }

  public static void clear() {
    currentUserId.remove();
  }
}
