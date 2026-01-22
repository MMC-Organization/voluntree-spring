package com.voluntree.backend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.voluntree.backend.domain.CustomUserDetails;
import com.voluntree.backend.dto.user.PasswordUpdateRequest;
import com.voluntree.backend.dto.user.ProfileResponse;
import com.voluntree.backend.dto.user.UpdateRequest; 
import com.voluntree.backend.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
private final UserService userService;


@GetMapping("/me")
public ResponseEntity<ProfileResponse> getMyProfile(@AuthenticationPrincipal CustomUserDetails currentUser) {
    
    // Pega o ID diretamente do token/sessão
    ProfileResponse profile = userService.getProfile(currentUser.getUserId());
    return ResponseEntity.ok(profile);
}

@PutMapping("/me")
    public ResponseEntity<Void> update(@AuthenticationPrincipal CustomUserDetails currentUser, @Valid @RequestBody UpdateRequest dto) {

        // currentUser.getUserId() garante que o usuário só altere a si mesmo
        userService.updateUser(currentUser.getUserId(), dto);
        
        return ResponseEntity.noContent().build();
    }

@PatchMapping("/me/password")
public ResponseEntity<Void> updatePassword(
    @AuthenticationPrincipal CustomUserDetails currentUser,
    @Valid @RequestBody PasswordUpdateRequest dto
) {
    userService.updatePassword(currentUser.getUserId(), dto);
    return ResponseEntity.noContent().build();
}
}

