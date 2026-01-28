package com.voluntree.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.voluntree.backend.dto.auth.AuthenticationRequest;
import com.voluntree.backend.dto.auth.AuthenticationResponse;
import com.voluntree.backend.dto.auth.AuthenticationStatusResponse;
import com.voluntree.backend.dto.signup.OrganizationRequest;
import com.voluntree.backend.dto.signup.OrganizationResponse;
import com.voluntree.backend.dto.signup.VolunteerRequest;
import com.voluntree.backend.dto.signup.VolunteerResponse;
import com.voluntree.backend.service.AuthService;
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
  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest body,
      HttpServletRequest request, HttpServletResponse response) {

    Long userId = authService.authenticate(body, request, response);
    return ResponseEntity.ok(new AuthenticationResponse(userId));
  }

  @GetMapping
  public ResponseEntity<AuthenticationStatusResponse> authStatus(Authentication auth) {
    AuthenticationStatusResponse authState = authService.isAuthenticated(auth);

    if (authState.status()) {
      return ResponseEntity.ok(authState);
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authState);
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