package com.voluntree.backend.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import com.voluntree.backend.domain.CustomUserDetails;
import com.voluntree.backend.dto.auth.AuthenticationRequest;
import com.voluntree.backend.dto.auth.AuthenticationResponse;
import com.voluntree.backend.dto.auth.AuthenticationStatusResponse;
import com.voluntree.backend.enums.ActionType;
import com.voluntree.backend.enums.Module;
import com.voluntree.backend.enums.UserType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final LogService logService;
  private final AuthenticationManager authManager;
  private final TokenService tokenService;
  private final SecurityContextRepository securityContextRepo;
  private final SecurityContextHolderStrategy securityContextHolderStrat = SecurityContextHolder
      .getContextHolderStrategy();

  public AuthenticationResponse authenticate(AuthenticationRequest body, HttpServletRequest request, HttpServletResponse response) {
    Authentication authRequest = UsernamePasswordAuthenticationToken.unauthenticated(body.email(), body.password());
    Authentication authResponse;

    try {
      authResponse = this.authManager.authenticate(authRequest);

      //SecurityContext context = securityContextHolderStrat.createEmptyContext();
      //context.setAuthentication(authResponse);
      //securityContextHolderStrat.setContext(context);
      //securityContextRepo.saveContext(context, request, response);

      CustomUserDetails user = (CustomUserDetails) authResponse.getPrincipal();
      String token = tokenService.generateToken(user);
      logService.saveSuccessLog("Houve um novo acesso!", null, user.getUserId(), user.getUserType(),
          ActionType.SIGNIN, Module.AUTH);

      return new AuthenticationResponse(
          user.getUserId(),
          token,
          user.getUsername(), 
          user.getUserType().toString()
      );
    } catch (AuthenticationException e) {
      logService.saveAccessFailureLog("Houve uma nova tentativa de acesso (" + e.getMessage() + ")!", null, null,
          body.email(), null,
          ActionType.SIGNIN, Module.AUTH);
      throw e;
    }
  }

  public AuthenticationStatusResponse isAuthenticated(Authentication auth) {
    boolean status = false;
    UserType userType = null;
    String message = "Usuário não autenticado!";

    if (auth != null && auth.getPrincipal() instanceof CustomUserDetails) {
      status = true;
      userType = ((CustomUserDetails) auth.getPrincipal()).getUserType();
      message = "Usuário autenticado!";
    }

    return new AuthenticationStatusResponse(message, status, userType);
  }
}
