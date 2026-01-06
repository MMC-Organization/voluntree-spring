package com.voluntree.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.voluntree.backend.domain.organization.Organization;
import com.voluntree.backend.domain.volunteer.Volunteer;
import com.voluntree.backend.dto.registration.OrganizationRegistration;
import com.voluntree.backend.dto.registration.OrganizationResponse;
import com.voluntree.backend.dto.registration.VolunteerRegistration;
import com.voluntree.backend.dto.registration.VolunteerResponse;
import com.voluntree.backend.service.RegistrationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/registration")
@RequiredArgsConstructor
public class RegistrationController {
private final RegistrationService registrationService;

    @PostMapping("/volunteer")
    public ResponseEntity<VolunteerResponse> signupVolunteer(@RequestBody VolunteerRegistration dto) {
        Volunteer newVolunteer = registrationService.registerVolunteer(dto);
        VolunteerResponse response = new VolunteerResponse(
                            newVolunteer.getName(),
                            newVolunteer.getEmail()
                        );
                
        return ResponseEntity.status(HttpStatus.CREATED).body(response);    }

    @PostMapping("/organization")
    public ResponseEntity<OrganizationResponse> signupOrganization(@RequestBody OrganizationRegistration dto) {
        Organization newOrg = registrationService.registerOrganization(dto);
            OrganizationResponse response = new OrganizationResponse(
                            newOrg.getName(),
                            newOrg.getEmail(),
                            newOrg.getPhoneNumber(),
                            newOrg.getCep(),
                            newOrg.getCompanyName(),
                            newOrg.getCause()
                        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
