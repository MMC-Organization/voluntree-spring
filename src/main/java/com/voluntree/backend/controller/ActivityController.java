package com.voluntree.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.voluntree.backend.domain.CustomUserDetails;
import com.voluntree.backend.dto.activity.ActivityListResponse;
import com.voluntree.backend.dto.activity.ActivityRequest;
import com.voluntree.backend.dto.activity.ActivityResponse;
import com.voluntree.backend.dto.activity.ActivityUpdateRequest;
import com.voluntree.backend.service.ActivityService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

   
    @PostMapping
    @PreAuthorize("hasRole('ORGANIZATION')")
    public ResponseEntity<ActivityResponse> createActivity(@RequestBody @Valid ActivityRequest request, @AuthenticationPrincipal CustomUserDetails user) {
        Long organizationId = user.getUserId();

        ActivityResponse response = activityService.createActivity(request, organizationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

  
    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponse> getActivityById(@PathVariable Long id) {
        ActivityResponse response = activityService.getActivityById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<ActivityListResponse>> getAllActivities(
            @PageableDefault(size = 20, sort = "activityDate") Pageable pageable) {
        Page<ActivityListResponse> activities = activityService.getAllActivities(pageable);
        return ResponseEntity.ok(activities);
    }

    
    @GetMapping("/upcoming")
    public ResponseEntity<Page<ActivityListResponse>> getUpcomingActivities(
            @PageableDefault(size = 20, sort = "activityDate") Pageable pageable) {
        Page<ActivityListResponse> activities = activityService.getUpcomingActivities(pageable);
        return ResponseEntity.ok(activities);
    }


    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<Page<ActivityListResponse>> getActivitiesByOrganization(
            @PathVariable Long organizationId,
            @PageableDefault(size = 20, sort = "activityDate") Pageable pageable) {
        Page<ActivityListResponse> activities = activityService.getActivitiesByOrganization(organizationId, pageable);
        return ResponseEntity.ok(activities);
    }

   
    @GetMapping("/my-activities")
    public ResponseEntity<Page<ActivityListResponse>> getMyActivities(
            @PageableDefault(size = 20, sort = "activityDate") Pageable pageable,  @AuthenticationPrincipal CustomUserDetails user) {
        Long organizationId = user.getUserId();

        Page<ActivityListResponse> activities = activityService.getActivitiesByOrganization(organizationId, pageable);
        return ResponseEntity.ok(activities);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZATION')")
    public ResponseEntity<ActivityResponse> updateActivity(
            @PathVariable Long id,
            @RequestBody @Valid ActivityUpdateRequest request, @AuthenticationPrincipal CustomUserDetails user) {
        Long organizationId = user.getUserId();

        ActivityResponse response = activityService.updateActivity(id, request, organizationId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ORGANIZATION')")
    public ResponseEntity<Void> cancelActivity(@PathVariable Long id,  @AuthenticationPrincipal CustomUserDetails user) {
    
        Long organizationId = user.getUserId();

        activityService.cancelActivity(id, organizationId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ORGANIZATION')")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {
        Long organizationId = user.getUserId();

        activityService.deleteActivity(id, organizationId);
        return ResponseEntity.noContent().build();
    }
}

