package com.voluntree.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.voluntree.backend.domain.volunteer.Volunteer;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

}
