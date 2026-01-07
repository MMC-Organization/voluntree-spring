package com.voluntree.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.voluntree.backend.domain.Registration;
import java.util.Optional;
import java.util.List;


public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByVolunteerIdAndActivityIdAndCanceledFalse(Long volunteerId, Long activityId);
    List<Registration> findByVolunteerIdAndCanceledFalse(Long volunteerId);
    Optional<Registration> findByVolunteerIdAndActivityId(Long volunteerId, Long activityId);
}
