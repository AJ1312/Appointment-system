package com.ajitesh.medical_appointment_system.repository;

import com.ajitesh.medical_appointment_system.entity.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Integer> {
}
