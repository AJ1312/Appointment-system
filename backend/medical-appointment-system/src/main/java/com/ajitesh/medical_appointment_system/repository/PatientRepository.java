package com.ajitesh.medical_appointment_system.repository;

import com.ajitesh.medical_appointment_system.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Optional<Patient> findByEmail(String email);
    boolean existsByEmail(String email);
}
