package com.voluntree.backend.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.voluntree.backend.domain.CustomUserDetails;
import com.voluntree.backend.dto.auth.AuthenticationRequest;
import com.voluntree.backend.dto.auth.AuthenticationResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthenticationManager authManager;
  private final SecurityContextRepository securityContextRepo;
  private final SecurityContextHolderStrategy securityContextHolderStrat = SecurityContextHolder
      .getContextHolderStrategy();

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest body,
      HttpServletRequest request, HttpServletResponse response) {
    Authentication authRequest = UsernamePasswordAuthenticationToken.unauthenticated(body.email(), body.password());
    Authentication authResponse = this.authManager.authenticate(authRequest);

    SecurityContext context = securityContextHolderStrat.createEmptyContext();
    context.setAuthentication(authResponse);
    securityContextHolderStrat.setContext(context);
    securityContextRepo.saveContext(context, request, response);

    CustomUserDetails user = (CustomUserDetails) authResponse.getPrincipal();

    return ResponseEntity.ok(new AuthenticationResponse(user.getUser().getId()));
  }
}
