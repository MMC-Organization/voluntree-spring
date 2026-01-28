package com.voluntree.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voluntree.backend.domain.User;
import com.voluntree.backend.domain.organization.Cnpj;
import com.voluntree.backend.domain.organization.Organization;
import com.voluntree.backend.domain.volunteer.Cpf;
import com.voluntree.backend.domain.volunteer.Volunteer;
import com.voluntree.backend.dto.signup.OrganizationRequest;
import com.voluntree.backend.dto.signup.OrganizationResponse;
import com.voluntree.backend.dto.signup.VolunteerRequest;
import com.voluntree.backend.dto.signup.VolunteerResponse;
import com.voluntree.backend.dto.user.PasswordUpdateRequest;
import com.voluntree.backend.dto.user.ProfileResponse;
import com.voluntree.backend.dto.user.UpdateRequest;
import com.voluntree.backend.enums.ActionType;
import com.voluntree.backend.enums.Module;
import com.voluntree.backend.enums.UserType;
import com.voluntree.backend.repository.OrganizationRepository;
import com.voluntree.backend.repository.UserRepository;
import com.voluntree.backend.repository.VolunteerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final VolunteerRepository volunteerRepository;
  private final OrganizationRepository organizationRepository;
  private final LogService logService;
  private final UserRepository userRepository;

  @Transactional
  public VolunteerResponse registerVolunteer(VolunteerRequest data) {
    try {

      if (volunteerRepository.existsByCpf(new Cpf(data.cpf()))) {
        throw new IllegalArgumentException("Este CPF já está cadastrado.");
      }

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
      if (organizationRepository.existsByCnpj(new Cnpj(data.cnpj()))) {
        throw new IllegalArgumentException("Este CNPJ já está cadastrado.");
      }
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

  public ProfileResponse getProfile(Long id) {

    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    String userType = (user instanceof Volunteer) ? "VOLUNTEER" : "ORGANIZATION";
    String cpf = null;
    String cnpj = null;
    String companyName = null;
    String cause = null;

    if (user instanceof Volunteer v) {
      cpf = v.getCpf().getCpf();
    }
 
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
        cause);
  }

  @Transactional
  public void updateUser(Long id, UpdateRequest dto) {
    try {
      User user = userRepository.findById(id)
          .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

      String novoEmail = dto.email();

      
      if (!novoEmail.equals(user.getEmail())) {
        if (userRepository.existsByEmail(novoEmail)) {
          throw new RuntimeException("Este e-mail já está em uso por outro usuário");
        }
        user.setEmail(novoEmail);
      }

      if (dto.name() != null) {
        user.setName(dto.name());
      }

      if (dto.phoneNumber() != null) {
        user.setPhoneNumber(dto.phoneNumber());
      }

      if (dto.cep() != null) {
        user.setCep(dto.cep());
      }

      if (dto.number() != null) {
        user.setNumber(dto.number());
      }

      userRepository.save(user);

      UserType type = (user instanceof Volunteer) ? UserType.VOLUNTEER : UserType.ORGANIZATION;

      logService.saveSuccessLog("Perfil atualizado com sucesso!", id, id, type,
          ActionType.UPDATE, Module.PROFILE);
    } catch (Exception e) {
      logService.saveFailureLog("Erro atualizando perfil: " + e.getMessage() + "!", null, id, null,
          ActionType.UPDATE, Module.PROFILE);
      throw e;
    }
  }

  @Transactional
  public void updatePassword(Long id, PasswordUpdateRequest dto) {
    try {
      User user = userRepository.findById(id)
          .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

      if (!passwordEncoder.matches(dto.oldPassword(), user.getPassword())) {
        throw new RuntimeException("A senha atual está incorreta");
      }

      user.setPassword(passwordEncoder.encode(dto.newPassword()));
      userRepository.save(user);

      UserType type = (user instanceof Volunteer) ? UserType.VOLUNTEER : UserType.ORGANIZATION;

      logService.saveSuccessLog("Senha atualizada com sucesso!", id, id, type,
          ActionType.UPDATE, Module.PROFILE);
    } catch (Exception e) {
      logService.saveFailureLog("Erro atualizando senha: " + e.getMessage() + "!", null, id, null,
          ActionType.UPDATE, Module.PROFILE);
      throw e;
    }
  }

}
