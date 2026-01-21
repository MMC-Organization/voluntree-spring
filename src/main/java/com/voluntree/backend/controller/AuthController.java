package com.voluntree.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.voluntree.backend.domain.CustomUserDetails;
import com.voluntree.backend.dto.auth.AuthenticationRequest;
import com.voluntree.backend.dto.auth.AuthenticationResponse;
import com.voluntree.backend.dto.auth.AuthenticationStatusResponse;
import com.voluntree.backend.dto.signup.OrganizationRequest;
import com.voluntree.backend.dto.signup.OrganizationResponse;
import com.voluntree.backend.dto.signup.VolunteerRequest;
import com.voluntree.backend.dto.signup.VolunteerResponse;
import com.voluntree.backend.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;
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

    return ResponseEntity.ok(new AuthenticationResponse(user.getUserId()));
  }

  @GetMapping
  public ResponseEntity<AuthenticationStatusResponse> authStatus(Authentication auth) {
    if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails))
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new AuthenticationStatusResponse("Usuário não autenticado!", false));

    return ResponseEntity.ok(new AuthenticationStatusResponse("Usuário autenticado!", true));
  }

  @PostMapping("/signup/volunteer")
  public ResponseEntity<VolunteerResponse> signupVolunteer(@RequestBody @Valid VolunteerRequest dto) {

    VolunteerResponse response = userService.registerVolunteer(dto);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/signup/organization")
  public ResponseEntity<OrganizationResponse> signupOrganization(@RequestBody @Valid OrganizationRequest dto) {

    OrganizationResponse response = userService.registerOrganization(dto);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}