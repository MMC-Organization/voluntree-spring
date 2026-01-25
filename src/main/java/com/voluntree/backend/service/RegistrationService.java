package com.voluntree.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voluntree.backend.domain.Activity;
import com.voluntree.backend.domain.Registration;
import com.voluntree.backend.domain.volunteer.Volunteer;
import com.voluntree.backend.dto.RegistrationDTO;
import com.voluntree.backend.enums.ActionType;
import com.voluntree.backend.enums.Module;
import com.voluntree.backend.enums.UserType;
import com.voluntree.backend.repository.ActivityRepository;
import com.voluntree.backend.repository.RegistrationRepository;
import com.voluntree.backend.repository.VolunteerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrationService {

  private final RegistrationRepository registrationRepository;
  private final ActivityRepository activityRepository;
  private final VolunteerRepository volunteerRepository;
  private final LogService logService;

  @Transactional
  public void subscribe(Long volunteerId, Long activityId) {
    try {
      Optional<Registration> existingRegistration = registrationRepository.findByVolunteerIdAndActivityId(volunteerId,
          activityId);

      if (!existingRegistration.isPresent()) {
        Volunteer volunteer = volunteerRepository.findById(volunteerId)
            .orElseThrow(() -> new RuntimeException("Voluntário não encontrado"));

        Activity activity = activityRepository.findById(activityId)
            .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        Registration registration = new Registration();
        registration.setVolunteer(volunteer);
        registration.setActivity(activity);
        registration.setCanceled(false);

        registrationRepository.save(registration);

        logService.saveSuccessLog("Usuário se inscreveu na atividade!", volunteerId, activityId,
            UserType.VOLUNTEER,
            ActionType.CREATE, Module.REGISTRATION);
        return;
      }

      Registration registration = existingRegistration.get();

      if (!registration.getCanceled()) {
        throw new IllegalArgumentException("Você já está inscrito nesta atividade.");
      }

      registration.setCanceled(false);

      registrationRepository.save(registration);

      logService.saveSuccessLog("Usuário refez inscrição na atividade!", volunteerId, activityId, UserType.VOLUNTEER,
          ActionType.UPDATE, Module.REGISTRATION);
    } catch (Exception e) {
      logService.saveFailureLog("Erro ao tentar se inscrever em atividade: " + e.getMessage(), volunteerId, activityId,
          UserType.VOLUNTEER, ActionType.CREATE, Module.REGISTRATION);
      throw e;
    }
  }

  @Transactional
  public void unsubscribe(Long volunteerId, Long activityId) {
    try {
      Registration registration = registrationRepository.findByVolunteerIdAndActivityId(volunteerId, activityId)
          .orElseThrow(() -> new RuntimeException("Inscrição não encontrada."));

      registration.setCanceled(true);
      registrationRepository.save(registration);

      logService.saveSuccessLog("Inscrição cancelada na atividade!", volunteerId, activityId, UserType.VOLUNTEER,
          ActionType.UPDATE, Module.REGISTRATION);
    } catch (Exception e) {
      logService.saveFailureLog("Erro no cancelamento da inscrição da atividade: " + e.getMessage(), volunteerId,
          activityId, UserType.VOLUNTEER,
          ActionType.UPDATE, Module.REGISTRATION);
      throw e;
    }
  }

  public List<RegistrationDTO> getMyRegistrations(Long volunteerId) {
    return registrationRepository.findByVolunteerIdAndCanceledFalse(volunteerId)
        .stream()
        .map(reg -> new RegistrationDTO(
            reg.getId(),
            reg.getActivity().getId(),
            reg.getActivity().getName(),
            reg.getVolunteer().getName(),
            reg.getRegisteredAt(),
            reg.getCanceled()))
        .collect(Collectors.toList());
  }
}
