package com.voluntree.backend.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.voluntree.backend.domain.organization.Cnpj;
import com.voluntree.backend.domain.organization.Organization;
import com.voluntree.backend.domain.volunteer.Cpf;
import com.voluntree.backend.domain.volunteer.Volunteer;
import com.voluntree.backend.dto.signup.OrganizationRequest;
import com.voluntree.backend.dto.signup.OrganizationResponse;
import com.voluntree.backend.dto.signup.VolunteerRequest;
import com.voluntree.backend.dto.signup.VolunteerResponse;
import com.voluntree.backend.enums.ActionType;
import com.voluntree.backend.enums.Outcome;
import com.voluntree.backend.enums.UserType;
import com.voluntree.backend.enums.Module;
import com.voluntree.backend.events.AuditEvent;
import com.voluntree.backend.repository.OrganizationRepository;
import com.voluntree.backend.repository.VolunteerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final VolunteerRepository volunteerRepository;
    private final OrganizationRepository organizationRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public VolunteerResponse registerVolunteer(VolunteerRequest data) {
        Volunteer volunteer = new Volunteer();
        
        volunteer.setName(data.name());
        volunteer.setEmail(data.email());
        volunteer.setPassword(passwordEncoder.encode(data.password()));          
        volunteer.setPhoneNumber(data.phoneNumber());
        volunteer.setCep(data.cep());
        volunteer.setNumber(data.number());
        volunteer.setCpf(new Cpf(data.cpf())); 

        Volunteer savedVolunteer = volunteerRepository.save(volunteer);

        AuditEvent event = new AuditEvent(
            "Novo voluntário cadastrado: " + savedVolunteer.getEmail(), 
            savedVolunteer.getId(), 
            savedVolunteer.getId(), 
            UserType.VOLUNTEER,     
            ActionType.CREATE,     
            Outcome.SUCCESS,        
            Module.AUTH     
        );
        
        eventPublisher.publishEvent(event);
    

        return new VolunteerResponse(
            savedVolunteer.getName(),
            savedVolunteer.getEmail()
        );
    }

    @Transactional
    public OrganizationResponse registerOrganization(OrganizationRequest data) {
        Organization organization = new Organization();

        
        organization.setName(data.name());
        organization.setEmail(data.email());
        organization.setPassword(passwordEncoder.encode(data.password())); 
        organization.setPhoneNumber(data.phoneNumber());
        organization.setCep(data.cep());
        organization.setNumber(data.number());

  
        organization.setCnpj(new Cnpj(data.cnpj()));
        organization.setCompanyName(data.companyName());
        organization.setCause(data.cause());

        Organization savedOrganization = organizationRepository.save(organization);

        AuditEvent event = new AuditEvent(
            "Nova organização cadastrada: " + savedOrganization.getCompanyName(),
            savedOrganization.getId(),
            savedOrganization.getId(),
            UserType.ORGANIZATION, 
            ActionType.CREATE,
            Outcome.SUCCESS,
            Module.AUTH
        );

        eventPublisher.publishEvent(event);

       return new OrganizationResponse(
            savedOrganization.getName(),       
            savedOrganization.getEmail(),
            savedOrganization.getPhoneNumber(),
            savedOrganization.getCep(),
            savedOrganization.getCompanyName(), 
            savedOrganization.getCause()
        );
    }
}