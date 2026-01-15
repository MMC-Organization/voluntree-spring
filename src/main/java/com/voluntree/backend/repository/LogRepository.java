package com.voluntree.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.voluntree.backend.domain.Log;

public interface LogRepository extends JpaRepository<Log, Long> {
    Page<Log> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
