package com.voluntree.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.voluntree.backend.domain.organization.Cnpj;
import com.voluntree.backend.domain.organization.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Boolean existsByCnpj(Cnpj cnpj);
}
