package com.voluntree.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voluntree.backend.domain.Activity;
import com.voluntree.backend.domain.Registration;
import com.voluntree.backend.domain.volunteer.Volunteer; 
import com.voluntree.backend.dto.RegistrationDTO   ;
import com.voluntree.backend.enums.ActionType;
import com.voluntree.backend.enums.Outcome;
import com.voluntree.backend.enums.UserType;
import com.voluntree.backend.events.AuditEvent;
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
    private final ApplicationEventPublisher eventPublisher;

    
    @Transactional
    public void subscribe(Long volunteerId, Long activityId) {
        
     
        var existingRegistration = registrationRepository.findByVolunteerIdAndActivityId(volunteerId, activityId);
        if (existingRegistration.isPresent()) {
            Registration registration = existingRegistration.get();
            if (!registration.getCanceled()) {
                throw new IllegalArgumentException("Você já está inscrito nesta atividade.");
            }
            
            
            registration.setCanceled(false);
            registrationRepository.save(registration);
            
            publishLog(volunteerId, activityId, "RE-INSCRIÇÃO");
            return; 
        }

        
        Volunteer volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException("Voluntário não encontrado"));

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        
        Registration registration = new Registration();
        registration.setVolunteer(volunteer);
        registration.setActivity(activity);
        registration.setCanceled(false);
       
        registrationRepository.save(registration);

        
       publishLog(volunteerId, activityId, "Inscrição realizada na atividade " + activity.getName());
    }

  
    @Transactional
    public void unsubscribe(Long volunteerId, Long activityId) {
        Registration registration = registrationRepository.findByVolunteerIdAndActivityId(volunteerId, activityId)
                .orElseThrow(() -> new RuntimeException("Inscrição não encontrada."));


        registration.setCanceled(true);
        registrationRepository.save(registration);

      
        publishLog(volunteerId, activityId, "Inscrição cancelada na atividade " + activityId);
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
                        reg.getCanceled()
                ))
                .collect(Collectors.toList());
    }

  
    private void publishLog(Long userId, Long resourceId, String mensagem) {
        
        AuditEvent event = new AuditEvent(
            mensagem,
            userId,
            resourceId,
            UserType.VOLUNTEER,
            ActionType.CREATE, 
            Outcome.SUCCESS,    
            com.voluntree.backend.enums.Module.REGISTRATION
        );

        
        eventPublisher.publishEvent(event);
    }
}
