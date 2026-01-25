package com.voluntree.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.voluntree.backend.enums.UserType;
import com.voluntree.backend.repository.OrganizationRepository;
import com.voluntree.backend.repository.VolunteerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final VolunteerRepository volunteerRepository;
  private final OrganizationRepository organizationRepository;
  private final LogService logService;

  @Transactional
  public VolunteerResponse registerVolunteer(VolunteerRequest data) {
    try {
      Volunteer volunteer = new Volunteer();

      volunteer.setName(data.name());
      volunteer.setEmail(data.email());
      volunteer.setPassword(passwordEncoder.encode(data.password()));
      volunteer.setPhoneNumber(data.phoneNumber());
      volunteer.setCep(data.cep());
      volunteer.setNumber(data.number());
      volunteer.setCpf(new Cpf(data.cpf()));

      Volunteer savedVolunteer = volunteerRepository.save(volunteer);

      logService.saveSuccessLog("Novo voluntário cadastrado!", null, savedVolunteer.getId(), UserType.VOLUNTEER,
          ActionType.CREATE, Module.AUTH);

      return new VolunteerResponse(
          savedVolunteer.getName(),
          savedVolunteer.getEmail());
    } catch (Exception e) {
      logService.saveFailureLog("Erro criando voluntário de email " + data.email() + ": " + e.getMessage(), null, null,
          UserType.VOLUNTEER, ActionType.CREATE, Module.AUTH);
      throw e;
    }
  }

  @Transactional
  public OrganizationResponse registerOrganization(OrganizationRequest data) {
    try {
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

      logService.saveSuccessLog("Nova organização cadastrada!", null, savedOrganization.getId(), UserType.ORGANIZATION,
          ActionType.CREATE, Module.AUTH);

      return new OrganizationResponse(
          savedOrganization.getName(),
          savedOrganization.getEmail(),
          savedOrganization.getPhoneNumber(),
          savedOrganization.getCep(),
          savedOrganization.getCompanyName(),
          savedOrganization.getCause());
    } catch (Exception e) {
      logService.saveFailureLog("Erro criando organização de email " + data.email() + ": " + e.getMessage(), null, null,
          UserType.ORGANIZATION, ActionType.CREATE, Module.AUTH);
      throw e;
    }
  }
}