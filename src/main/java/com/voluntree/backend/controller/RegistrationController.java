package com.voluntree.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.voluntree.backend.dto.RegistrationDTO;
import com.voluntree.backend.service.RegistrationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping("/activity/{activityId}")
    public ResponseEntity<String> subscribe(@PathVariable Long activityId) {
     
        Long volunteerId = 1L; 

        try {
            registrationService.subscribe(volunteerId, activityId);
            return ResponseEntity.ok("Inscrição realizada com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

  
    @DeleteMapping("/activity/{activityId}")
    public ResponseEntity<String> unsubscribe(@PathVariable Long activityId) {
        Long volunteerId = 1L; // Mock
        
        try {
            registrationService.unsubscribe(volunteerId, activityId);
            return ResponseEntity.ok("Inscrição cancelada.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/my")
    public ResponseEntity<List<RegistrationDTO>> getMyRegistrations() {
        Long volunteerId = 1L; 
        return ResponseEntity.ok(registrationService.getMyRegistrations(volunteerId));
    }
}
