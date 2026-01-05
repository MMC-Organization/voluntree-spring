package com.voluntree.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.voluntree.backend.domain.organization.Organization;
import com.voluntree.backend.domain.volunteer.Volunteer;
import com.voluntree.backend.service.UserService;
import com.voluntree.backend.dto.registration.OrganizationRegistration;
import com.voluntree.backend.dto.registration.VolunteerRegistration;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
private final UserService userService;

    @PostMapping("/signup/volunteer")
    public ResponseEntity<Volunteer> signupVolunteer(@RequestBody VolunteerRegistration dto) {
        Volunteer newVolunteer = userService.registerVolunteer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newVolunteer);
    }

    @PostMapping("/signup/organization")
    public ResponseEntity<Organization> signupOrganization(@RequestBody OrganizationRegistration dto) {
        Organization newOrg = userService.registerOrganization(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrg);
    }

}
