package com.voluntree.backend.domain;

import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.voluntree.backend.domain.organization.Organization;
import com.voluntree.backend.domain.volunteer.Volunter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

  private final User user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (user instanceof Volunter) {
      return List.of(new SimpleGrantedAuthority("ROLE_VOLUNTEER"));
    }

    if (user instanceof Organization) {
      return List.of(new SimpleGrantedAuthority("ROLE_ORGANIZATION"));
    }

    throw new IllegalStateException("Tipo de usuário inválido!");
  }

  @Override
  public @Nullable String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }
  
}
