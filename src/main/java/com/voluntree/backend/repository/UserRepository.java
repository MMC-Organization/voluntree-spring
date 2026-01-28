package com.voluntree.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.voluntree.backend.domain.User;
import com.voluntree.backend.domain.organization.Cnpj;
import com.voluntree.backend.domain.volunteer.Cpf;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  Boolean existsByEmail(String email);
  Boolean existsByCpf(Cpf cpf);
    
  Boolean existsByCnpj(Cnpj cnpj);
}