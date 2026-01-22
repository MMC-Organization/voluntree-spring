package com.voluntree.backend.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.voluntree.backend.domain.Registration;


@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByVolunteerIdAndActivityIdAndCanceledFalse(Long volunteerId, Long activityId);
    List<Registration> findByVolunteerIdAndCanceledFalse(Long volunteerId);
    Optional<Registration> findByVolunteerIdAndActivityId(Long volunteerId, Long activityId);
}
