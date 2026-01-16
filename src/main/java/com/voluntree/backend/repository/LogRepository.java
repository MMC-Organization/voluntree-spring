package com.voluntree.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.voluntree.backend.domain.Log;
import com.voluntree.backend.enums.UserType;

public interface LogRepository extends JpaRepository<Log, Long> {
    Page<Log> findByUserIdAndUserTypeOrderByCreatedAtDesc(Long userId, UserType userType, Pageable pageable);
}
