package com.voluntree.backend.service;

import com.voluntree.backend.domain.Activity;
import com.voluntree.backend.domain.Log;
import com.voluntree.backend.domain.Registration;
import com.voluntree.backend.domain.volunteer.Volunteer; 
import com.voluntree.backend.dto.RegistrationDTO;

import com.voluntree.backend.enums.ActionType;
import com.voluntree.backend.enums.Outcome;
import com.voluntree.backend.enums.UserType;

import com.voluntree.backend.repository.ActivityRepository;
import com.voluntree.backend.repository.LogRepository;
import com.voluntree.backend.repository.RegistrationRepository;
import com.voluntree.backend.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final ActivityRepository activityRepository;
    private final VolunteerRepository volunteerRepository;
    private final LogRepository logRepository; 

    
    @Transactional
    public void subscribe(Long volunteerId, Long activityId) {
        
        if (registrationRepository.existsByVolunteerIdAndActivityIdAndCanceledFalse(volunteerId, activityId)) {
            throw new IllegalArgumentException("Você já está inscrito nesta atividade.");
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

        
        createLog(volunteerId, activityId, "INSCRIÇÃO", "Voluntário se inscreveu na atividade " + activity.getName());
    }

  
    @Transactional
    public void unsubscribe(Long volunteerId, Long activityId) {
        Registration registration = registrationRepository.findByVolunteerIdAndActivityId(volunteerId, activityId)
                .orElseThrow(() -> new RuntimeException("Inscrição não encontrada."));


        registration.setCanceled(true);
        registrationRepository.save(registration);

      
        createLog(volunteerId, activityId, "CANCELAMENTO", "Cancelou inscrição na atividade " + activityId);
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

  
    private void createLog(Long userId, Long resourceId, String acao, String mensagem) {
        try {
            Log log = new Log(
                null, // ID auto
                Instant.now(),
                mensagem,
                userId,
                resourceId,
                UserType.VOLUNTEER,
                ActionType.CREATE,  
                Outcome.SUCESS,   
                com.voluntree.backend.enums.Module.REGISTRATION  
            ); logRepository.save(log);
        } catch (Exception e) {
            System.err.println("Erro ao salvar log: " + e.getMessage());
        }
    }
}
