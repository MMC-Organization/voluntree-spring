package com.voluntree.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/auth")
public class CsrfController {

  @GetMapping("/csrf")
  public CsrfToken getCsrfToken(CsrfToken token) {
    return token;
  }

}
