package com.ajitesh.medical_appointment_system.service;

import com.ajitesh.medical_appointment_system.dto.PatientDTO;
import com.ajitesh.medical_appointment_system.dto.PatientRegistrationRequest;
import com.ajitesh.medical_appointment_system.entity.Patient;
import com.ajitesh.medical_appointment_system.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient registerPatient(PatientRegistrationRequest request) {
        if (patientRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered: " + request.getEmail());
        }
        Patient patient = new Patient();
        patient.setName(request.getName());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        // Simple hash - in production use BCrypt
        patient.setPasswordHash(simpleHash(request.getPassword()));
        if (request.getDateOfBirth() != null && !request.getDateOfBirth().isBlank()) {
            patient.setDateOfBirth(LocalDate.parse(request.getDateOfBirth()));
        }
        patient.setGender(request.getGender());
        return patientRepository.save(patient);
    }

    public Optional<Patient> login(String email, String password) {
        return patientRepository.findByEmail(email)
                .filter(p -> p.getPasswordHash().equals(simpleHash(password)));
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(Integer id) {
        return patientRepository.findById(id);
    }

    public PatientDTO toDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setPatientId(patient.getPatientId());
        dto.setName(patient.getName());
        dto.setEmail(patient.getEmail());
        dto.setPhone(patient.getPhone());
        dto.setDateOfBirth(patient.getDateOfBirth());
        dto.setGender(patient.getGender());
        return dto;
    }

    private String simpleHash(String input) {
        // SHA-256 simple hash for demo purposes
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return input;
        }
    }
}
