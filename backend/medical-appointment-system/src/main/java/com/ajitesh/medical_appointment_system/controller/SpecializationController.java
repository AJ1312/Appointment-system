package com.ajitesh.medical_appointment_system.controller;

import com.ajitesh.medical_appointment_system.entity.Specialization;
import com.ajitesh.medical_appointment_system.service.SpecializationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@org.springframework.web.bind.annotation.CrossOrigin(origins = "*")
public class SpecializationController {

    private final SpecializationService specializationService;

    public SpecializationController(
            SpecializationService specializationService) {

        this.specializationService = specializationService;
    }

    @GetMapping("/api/specializations")
    public List<Specialization> getAllSpecializations() {

        return specializationService.getAllSpecializations();
    }
}