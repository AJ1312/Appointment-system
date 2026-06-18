package com.ajitesh.medical_appointment_system.service;

import com.ajitesh.medical_appointment_system.entity.Specialization;
import com.ajitesh.medical_appointment_system.repository.SpecializationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecializationService {

    private final SpecializationRepository specializationRepository;

    public SpecializationService(
            SpecializationRepository specializationRepository) {

        this.specializationRepository = specializationRepository;
    }

    public List<Specialization> getAllSpecializations() {

        return specializationRepository.findAll();
    }
}