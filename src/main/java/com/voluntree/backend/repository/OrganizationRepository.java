package com.voluntree.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.voluntree.backend.domain.organization.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

}
