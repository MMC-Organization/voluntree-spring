package com.voluntree.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.voluntree.backend.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
