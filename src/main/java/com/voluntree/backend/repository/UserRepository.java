package com.voluntree.backend.repository;

import org.springframework.data.repository.Repository;

import com.voluntree.backend.domain.User;

public interface UserRepository extends Repository<User, Long> {

}
