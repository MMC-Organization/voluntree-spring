package com.voluntree.backend.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.voluntree.backend.domain.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
  

  Page<Activity> findByCanceledFalse(Pageable pageable);
  
  Page<Activity> findByOrganizationId(Long organizationId, Pageable pageable);
  
  @Query("SELECT a FROM Activity a WHERE a.activityDate > :now AND a.canceled = false")
  Page<Activity> findUpcomingActivities(@Param("now") LocalDateTime now, Pageable pageable);
  
  @Query("SELECT a FROM Activity a WHERE a.organization.id = :organizationId AND a.activityDate > :now")
  Page<Activity> findUpcomingActivitiesByOrganization(@Param("organizationId") Long organizationId, 
                                                        @Param("now") LocalDateTime now, 
                                                        Pageable pageable);
}

