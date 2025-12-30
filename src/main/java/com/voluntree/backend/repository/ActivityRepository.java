package com.voluntree.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.voluntree.backend.domain.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
  
}
