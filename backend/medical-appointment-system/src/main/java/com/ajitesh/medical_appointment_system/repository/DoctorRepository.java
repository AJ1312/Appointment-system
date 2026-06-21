package com.ajitesh.medical_appointment_system.repository;

import com.ajitesh.medical_appointment_system.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    List<Doctor> findBySpecialization_SpecializationId(Integer specializationId);
    List<Doctor> findBySpecialization_NameContainingIgnoreCase(String name);
    Optional<Doctor> findByEmail(String email);
}
