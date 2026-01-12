package com.voluntree.backend.controller;

import org.springframework.security.core.Authentication; 
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.voluntree.backend.domain.CustomUserDetails;
import com.voluntree.backend.dto.user.UpdateRequest; 
import com.voluntree.backend.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
private final UserService userService;

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProfile(
            @PathVariable Long id, 
            @RequestBody @Valid UpdateRequest dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Long loggedUserId = userDetails.getUserId();

        if (!loggedUserId.equals(id)) {
            throw new AccessDeniedException("Você não tem permissão para alterar este perfil.");
        }

        // Esse método ainda não existe
        userService.updateUser(id, dto);

        return ResponseEntity.noContent().build(); 
    }
}
