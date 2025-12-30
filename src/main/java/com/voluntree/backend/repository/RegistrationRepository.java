package com.voluntree.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.voluntree.backend.domain.Registration;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

}
