package com.voluntree.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.voluntree.backend.domain.CustomUserDetails;
import com.voluntree.backend.dto.RegistrationDTO;
import com.voluntree.backend.service.RegistrationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/registration")
@RequiredArgsConstructor
@PreAuthorize("hasRole('VOLUNTEER')")
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping("/activity/{activityId}")
    public ResponseEntity<String> subscribe(@PathVariable Long activityId , @AuthenticationPrincipal CustomUserDetails userDetails 
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado.");
        }

        try {
          
            Long volunteerId = userDetails.getUserId();
            
            registrationService.subscribe(volunteerId, activityId);
            return ResponseEntity.ok("Inscrição realizada com sucesso!");
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
          
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao realizar inscrição: " + e.getMessage());
        }
    }

  
    @DeleteMapping("/activity/{activityId}")
    public ResponseEntity<String> unsubscribe(@PathVariable Long activityId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Long volunteerId = userDetails.getUserId();
        registrationService.unsubscribe(volunteerId, activityId);
        return ResponseEntity.ok("Inscrição cancelada.");
    }


    @GetMapping("/my")
    public ResponseEntity<List<RegistrationDTO>> getMyRegistrations(@AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Long volunteerId = userDetails.getUserId();
        return ResponseEntity.ok(registrationService.getMyRegistrations(volunteerId));
    }
}
