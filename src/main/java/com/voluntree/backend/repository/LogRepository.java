package com.voluntree.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.voluntree.backend.domain.Log;

public interface LogRepository extends JpaRepository<Log, Long> {

}
