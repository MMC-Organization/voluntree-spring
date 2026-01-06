package com.voluntree.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voluntree.backend.dto.registration.OrganizationRegistration;
import com.voluntree.backend.dto.registration.VolunteerRegistration;

import com.voluntree.backend.domain.organization.Cnpj;
import com.voluntree.backend.domain.organization.Organization;
import com.voluntree.backend.domain.volunteer.Cpf;
import com.voluntree.backend.domain.volunteer.Volunteer;
import com.voluntree.backend.repository.OrganizationRepository;
import com.voluntree.backend.repository.VolunteerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrationService {


    private final VolunteerRepository volunteerRepository;
    private final OrganizationRepository organizationRepository;

    @Transactional
    public Volunteer registerVolunteer(VolunteerRegistration data) {
        Volunteer volunteer = new Volunteer();
        
        volunteer.setName(data.name());
        volunteer.setEmail(data.email());
        volunteer.setPhoneNumber(data.phoneNumber()); //criptografar depois 
        volunteer.setCep(data.cep());
        volunteer.setNumber(data.number());
        
     
        volunteer.setCpf(new Cpf(data.cpf())); 

        return volunteerRepository.save(volunteer);
    }

    @Transactional
    public Organization registerOrganization(OrganizationRegistration data) {
        Organization organization = new Organization();
        
        organization.setName(data.name());
        organization.setEmail(data.email());
        organization.setPassword(data.password()); // Criptografar depois 
        organization.setPhoneNumber(data.phoneNumber());
        organization.setCep(data.cep());
        organization.setNumber(data.number());

  
        organization.setCnpj(new Cnpj(data.cnpj()));
        organization.setCompanyName(data.companyName());
        organization.setCause(data.cause());

        return organizationRepository.save(organization);
    }
}