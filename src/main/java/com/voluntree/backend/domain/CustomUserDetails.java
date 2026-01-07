package com.voluntree.backend.domain;

import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

  private final Long userId;
  private final String email;
  private final String password;
  private final String userType;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (userType.equals("VOLUNTEER")) {
      return List.of(new SimpleGrantedAuthority("ROLE_VOLUNTEER"));
    }

    if (userType.equals("ORGANIZATION")) {
      return List.of(new SimpleGrantedAuthority("ROLE_ORGANIZATION"));
    }

    throw new IllegalStateException("Tipo de usuário inválido!");
  }

  @Override
  public @Nullable String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  public Long getUserId() {
    return this.userId;
  }
}
