package com.voluntree.backend.repository;

import org.springframework.data.repository.Repository;

import com.voluntree.backend.domain.Activity;

public interface ActivityRepository extends Repository<Activity, Long> {
  
}
