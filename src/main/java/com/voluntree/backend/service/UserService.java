package com.voluntree.backend.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.voluntree.backend.domain.User;
import com.voluntree.backend.domain.organization.Cnpj;
import com.voluntree.backend.domain.organization.Organization;
import com.voluntree.backend.domain.volunteer.Cpf;
import com.voluntree.backend.domain.volunteer.Volunteer;
import com.voluntree.backend.dto.profile.UpdateProfileRequest;
import com.voluntree.backend.dto.profile.UserResponse;
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
import com.voluntree.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
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

        publishLog(
            "Novo voluntário cadastrado: " + savedVolunteer.getEmail(), 
            savedVolunteer.getId(), 
            savedVolunteer.getId(), 
            UserType.VOLUNTEER,     
            ActionType.CREATE,  
            Module.AUTH 
        );

        
        
        
    

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

        publishLog(
            "Nova organização cadastrada: " + savedOrganization.getCompanyName(),
            savedOrganization.getId(),
            savedOrganization.getId(),
            UserType.ORGANIZATION, 
            ActionType.CREATE,
            Module.AUTH
        );


       return new OrganizationResponse(
            savedOrganization.getName(),       
            savedOrganization.getEmail(),
            savedOrganization.getPhoneNumber(),
            savedOrganization.getCep(),
            savedOrganization.getCompanyName(), 
            savedOrganization.getCause()
        );
    }
    
    @Transactional
    public UserResponse updateUser(Long userId, UpdateProfileRequest data) {
        User user = userRepository.findById(userId) //dando erro aqui 
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (data.name() != null && !data.name().isBlank()) {
            user.setName(data.name());
        }
        if (data.phoneNumber() != null && !data.phoneNumber().isBlank()) {
            user.setPhoneNumber(data.phoneNumber());
        }
        if (data.cep() != null && !data.cep().isBlank()) {
            user.setCep(data.cep());
        }
        if (data.number() != null && !data.number().isBlank()) {
            user.setNumber(data.number());
        }
        if (data.email() != null && !data.email().isBlank()) {
            user.setEmail(data.email());
        }

        User updatedUser = userRepository.save(user);

       
        UserType userType = (user instanceof Volunteer) ? UserType.VOLUNTEER : UserType.ORGANIZATION;

        publishLog(
            "Perfil atualizado com sucesso",
            updatedUser.getId(),
            updatedUser.getId(),
            userType,
            ActionType.UPDATE,
            Module.PROFILE 
        );

        return new UserResponse(
            updatedUser.getId(),
            updatedUser.getName(),
            updatedUser.getEmail(),
            updatedUser.getPhoneNumber(),
            updatedUser.getCep()
        );
    }

    private void publishLog(String msg, Long userId, Long resourceId, UserType userType, ActionType action, Module module) {
        AuditEvent event = new AuditEvent(
            msg,
            userId,
            resourceId,
            userType,
            action,
            Outcome.SUCCESS, 
            module
        );
        eventPublisher.publishEvent(event);
    }
}