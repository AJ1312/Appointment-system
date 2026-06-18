package com.ajitesh.medical_appointment_system.repository;

import com.ajitesh.medical_appointment_system.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByPatient_PatientId(Integer patientId);
    List<Appointment> findByDoctor_DoctorId(Integer doctorId);
    List<Appointment> findByDoctor_DoctorIdAndAppointmentDate(Integer doctorId, java.time.LocalDate date);
    long countByDoctor_DoctorIdAndAppointmentDate(Integer doctorId, java.time.LocalDate date);
}
