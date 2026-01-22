package com.voluntree.backend.config;

import com.voluntree.backend.config.security.RlsContext;
import jakarta.persistence.EntityManager;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RlsAspect {

  private final EntityManager entityManager;

  public RlsAspect(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Before("@within(org.springframework.stereotype.Repository) || " +
          "@annotation(org.springframework.transaction.annotation.Transactional) || " +
          "@within(org.springframework.transaction.annotation.Transactional)")
  public void setRlsContext() {
    Long userId = RlsContext.getCurrentUserId();
    
    if (userId != null) {
      try {
        // Usa a função set_config do PostgreSQL que é segura contra SQL injection
        entityManager.createNativeQuery(
            "SELECT set_config('app.current_user_id', :userId, true)")
            .setParameter("userId", userId.toString())
            .getSingleResult();
      } catch (Exception e) {
        // Ignora erros silenciosamente se não houver transação ativa
        // Isso pode acontecer em operações read-only sem contexto transacional
      }
    }
  }
}
