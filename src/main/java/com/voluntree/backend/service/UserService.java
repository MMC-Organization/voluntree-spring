package com.voluntree.backend.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.voluntree.backend.domain.User;
import com.voluntree.backend.domain.organization.Cnpj;
import com.voluntree.backend.domain.organization.Organization;
import com.voluntree.backend.domain.volunteer.Cpf;
import com.voluntree.backend.domain.volunteer.Volunteer;
import com.voluntree.backend.dto.signup.OrganizationRequest;
import com.voluntree.backend.dto.signup.OrganizationResponse;
import com.voluntree.backend.dto.signup.VolunteerRequest;
import com.voluntree.backend.dto.signup.VolunteerResponse;
import com.voluntree.backend.enums.ActionType;
import com.voluntree.backend.enums.Module;
import com.voluntree.backend.enums.Outcome;
import com.voluntree.backend.enums.UserType;
import com.voluntree.backend.events.AuditEvent;
import com.voluntree.backend.repository.OrganizationRepository;
import com.voluntree.backend.repository.VolunteerRepository;
import com.voluntree.backend.repository.UserRepository;
import com.voluntree.backend.dto.user.PasswordUpdateRequest;
import com.voluntree.backend.dto.user.ProfileResponse;
import com.voluntree.backend.dto.user.UpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final VolunteerRepository volunteerRepository;
    private final UserRepository userRepository;
    
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
        Module.AUTH);

    eventPublisher.publishEvent(event);

    return new VolunteerResponse(
        savedVolunteer.getName(),
        savedVolunteer.getEmail());
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
        Module.AUTH);

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


     public ProfileResponse getProfile(Long id) {

        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String userType = (user instanceof Volunteer) ? "VOLUNTEER" : "ORGANIZATION";
        String cpf = null;
        String cnpj = null;
        String companyName = null;
        String cause = null;

        // Trata dados de Voluntário
        if (user instanceof Volunteer v) {
            cpf = v.getCpf().getCpf();
        } 
        // Trata dados de Organização
        else if (user instanceof Organization org) {
            cnpj = org.getCnpj().getCnpj();
            companyName = org.getCompanyName();
            cause = org.getCause();
        }

        return new ProfileResponse(
            user.getName(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getCep(),
            user.getNumber(),
            userType,
            cpf,
            cnpj,
            companyName,
            cause
        );
    }

    @Transactional
    public void updateUser(Long id, UpdateRequest dto) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String novoEmail = dto.email();
    
        // Só verificamos se o e-mail já existe se o usuário estiver tentando mudar para um e-mail DIFERENTE do atual
        if (!novoEmail.equals(user.getEmail())) {
            if (userRepository.existsByEmail(novoEmail)) {
                throw new RuntimeException("Este e-mail já está em uso por outro usuário");
            }
            user.setEmail(novoEmail);
        }

        user.setName(dto.name());
        user.setPhoneNumber(dto.phoneNumber());
        user.setCep(dto.cep());
        user.setNumber(dto.number());

        userRepository.save(user);

        UserType type = (user instanceof Volunteer) ? UserType.VOLUNTEER : UserType.ORGANIZATION;

    
        eventPublisher.publishEvent(new AuditEvent(
            "Perfil atualizado com sucesso", 
            id, 
            id, 
            type,     
            ActionType.UPDATE,     
            Outcome.SUCCESS,        
            Module.PROFILE     
        ));
       ;
        }


@Transactional
public void updatePassword(Long id, PasswordUpdateRequest dto) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    if (!passwordEncoder.matches(dto.oldPassword(), user.getPassword())) {
        throw new RuntimeException("A senha atual está incorreta");
    }

    user.setPassword(passwordEncoder.encode(dto.newPassword()));
    userRepository.save(user);

    UserType type = (user instanceof Volunteer) ? UserType.VOLUNTEER : UserType.ORGANIZATION;

    eventPublisher.publishEvent(new AuditEvent(
        "Senha alterada com sucesso", 
        id, id, type, 
        ActionType.UPDATE, 
        Outcome.SUCCESS, 
        Module.PROFILE
    ));
}

    }