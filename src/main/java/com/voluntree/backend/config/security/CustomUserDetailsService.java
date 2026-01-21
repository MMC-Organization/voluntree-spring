package com.voluntree.backend.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.voluntree.backend.domain.CustomUserDetails;
import com.voluntree.backend.domain.User;
import com.voluntree.backend.domain.volunteer.Volunteer;
import com.voluntree.backend.enums.UserType;
import com.voluntree.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository repo;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = repo.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário com e-mail \"" + email + "\" não foi encontrado"));

    UserType userType = (user instanceof Volunteer) ? UserType.VOLUNTEER : UserType.ORGANIZATION;

    return new CustomUserDetails(
        user.getId(),
        user.getEmail(),
        user.getPassword(),
        userType);
  }

}
