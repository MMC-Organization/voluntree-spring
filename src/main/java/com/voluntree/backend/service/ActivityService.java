package com.voluntree.backend.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voluntree.backend.domain.Activity;
import com.voluntree.backend.domain.organization.Organization;
import com.voluntree.backend.dto.activity.ActivityListResponse;
import com.voluntree.backend.dto.activity.ActivityRequest;
import com.voluntree.backend.dto.activity.ActivityResponse;
import com.voluntree.backend.dto.activity.ActivityUpdateRequest;
import com.voluntree.backend.enums.ActionType;
import com.voluntree.backend.enums.Module;
import com.voluntree.backend.enums.UserType;
import com.voluntree.backend.exception.ActivityNotFoundException;
import com.voluntree.backend.exception.InvalidUserTypeException;
import com.voluntree.backend.exception.UnauthorizedActivityAccessException;
import com.voluntree.backend.repository.ActivityRepository;
import com.voluntree.backend.repository.OrganizationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityService {

  private final ActivityRepository activityRepository;
  private final OrganizationRepository organizationRepository;
  private final LogService logService;

  @Transactional
  public ActivityResponse createActivity(ActivityRequest request, Long organizationId) {
    try {
      Organization organization = organizationRepository.findById(organizationId)
          .orElseThrow(() -> new InvalidUserTypeException("Organização não encontrada"));

      Activity activity = new Activity();
      activity.setName(request.name());
      activity.setDescription(request.description());
      activity.setSpots(request.spots());
      activity.setCep(request.cep());
      activity.setNumber(request.number());
      activity.setActivityDate(request.activityDate());
      activity.setOrganization(organization);
      activity.setCanceled(false);

      Activity savedActivity = activityRepository.save(activity);

      logService.saveSuccessLog("Atividade criada: " + savedActivity.getName(), organizationId, savedActivity.getId(),
          UserType.ORGANIZATION, ActionType.CREATE, Module.ACTIVITY);

      return mapToActivityResponse(savedActivity);
    } catch (Exception e) {
      logService.saveFailureLog("Erro criando atividade de nome " + request.name() + ": " + e.getMessage(),
          organizationId, null,
          UserType.ORGANIZATION, ActionType.CREATE, Module.ACTIVITY);
      throw e;
    }
  }

  @Transactional(readOnly = true)
  public ActivityResponse getActivityById(Long id) {
    Activity activity = activityRepository.findById(id)
        .orElseThrow(() -> new ActivityNotFoundException(id));

    return mapToActivityResponse(activity);
  }

  @Transactional(readOnly = true)
  public Page<ActivityListResponse> getAllActivities(Pageable pageable) {
    Page<Activity> activities = activityRepository.findByCanceledFalse(pageable);
    return activities.map(this::mapToActivityListResponse);
  }

  @Transactional(readOnly = true)
  public Page<ActivityListResponse> getUpcomingActivities(Pageable pageable) {
    Page<Activity> activities = activityRepository.findUpcomingActivities(
        LocalDateTime.now(), pageable);
    return activities.map(this::mapToActivityListResponse);
  }

  @Transactional(readOnly = true)
  public Page<ActivityListResponse> getActivitiesByOrganization(Long organizationId, Pageable pageable) {
    Page<Activity> activities = activityRepository.findByOrganizationId(organizationId, pageable);
    return activities.map(this::mapToActivityListResponse);
  }

  @Transactional
  public ActivityResponse updateActivity(Long activityId, ActivityUpdateRequest request, Long organizationId) {
    try {
      Activity activity = activityRepository.findById(activityId)
          .orElseThrow(() -> new ActivityNotFoundException(activityId));

      if (!activity.getOrganization().getId().equals(organizationId)) {
        throw new UnauthorizedActivityAccessException(
            "Você não tem permissão para atualizar esta atividade");
      }

      if (activity.getCanceled()) {
        throw new IllegalStateException("Não é possível atualizar uma atividade cancelada");
      }

      if (request.name() != null) {
        activity.setName(request.name());
      }
      if (request.description() != null) {
        activity.setDescription(request.description());
      }
      if (request.spots() != null) {
        activity.setSpots(request.spots());
      }
      if (request.cep() != null) {
        activity.setCep(request.cep());
      }
      if (request.number() != null) {
        activity.setNumber(request.number());
      }
      if (request.activityDate() != null) {
        activity.setActivityDate(request.activityDate());
      }

      Activity updatedActivity = activityRepository.save(activity);

      logService.saveSuccessLog("Atividade atualizada: " + updatedActivity.getName(), organizationId,
          activityId,
          UserType.ORGANIZATION, ActionType.UPDATE, Module.ACTIVITY);

      return mapToActivityResponse(updatedActivity);
    } catch (Exception e) {
      logService.saveFailureLog("Erro atualizando atividade: " + e.getMessage(), organizationId, activityId,
          UserType.ORGANIZATION, ActionType.UPDATE, Module.ACTIVITY);
      throw e;
    }
  }

  @Transactional
  public void cancelActivity(Long activityId, Long organizationId) {
    try {
      Activity activity = activityRepository.findById(activityId)
          .orElseThrow(() -> new ActivityNotFoundException(activityId));

      if (!activity.getOrganization().getId().equals(organizationId)) {
        throw new UnauthorizedActivityAccessException(
            "Você não tem permissão para cancelar esta atividade");
      }

      if (activity.getCanceled()) {
        throw new IllegalStateException("Esta atividade já foi cancelada");
      }

      activity.setCanceled(true);

      activityRepository.save(activity);

      logService.saveSuccessLog("Atividade cancelada: " + activity.getName(), organizationId,
          activityId,
          UserType.ORGANIZATION, ActionType.UPDATE, Module.ACTIVITY);
    } catch (Exception e) {
      logService.saveFailureLog("Erro ao cancelar atividade: " + e.getMessage(), organizationId,
          activityId,
          UserType.ORGANIZATION, ActionType.UPDATE, Module.ACTIVITY);
      throw e;
    }
  }

  @Transactional
  public void deleteActivity(Long activityId, Long organizationId) {
    try {
      Activity activity = activityRepository.findById(activityId)
          .orElseThrow(() -> new ActivityNotFoundException(activityId));

      if (!activity.getOrganization().getId().equals(organizationId)) {
        throw new UnauthorizedActivityAccessException(
            "Você não tem permissão para deletar esta atividade");
      }

      // Soft delete: apenas marca como cancelada ao invés de deletar fisicamente
      activity.setCanceled(true);
      activityRepository.save(activity);

      logService.saveSuccessLog("Atividade cancelada: " + activity.getName(), organizationId,
          activityId,
          UserType.ORGANIZATION, ActionType.DELETE, Module.ACTIVITY);
    } catch (Exception e) {
      logService.saveFailureLog("Erro ao cancelar atividade: " + e.getMessage(), organizationId,
          activityId,
          UserType.ORGANIZATION, ActionType.DELETE, Module.ACTIVITY);
      throw e;
    }
  }

  private ActivityResponse mapToActivityResponse(Activity activity) {
    return new ActivityResponse(
        activity.getId(),
        activity.getName(),
        activity.getDescription(),
        activity.getSpots(),
        activity.getCep(),
        activity.getNumber(),
        activity.getActivityDate(),
        activity.getOrganization().getId(),
        activity.getOrganization().getName(),
        activity.getOrganization().getCompanyName(),
        activity.getCanceled());
  }

  private ActivityListResponse mapToActivityListResponse(Activity activity) {
    return new ActivityListResponse(
        activity.getId(),
        activity.getName(),
        activity.getDescription(),
        activity.getSpots(),
        activity.getCep(),
        activity.getActivityDate(),
        activity.getOrganization().getName(),
        activity.getCanceled());
  }
}
