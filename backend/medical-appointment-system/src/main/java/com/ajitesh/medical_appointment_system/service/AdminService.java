package com.ajitesh.medical_appointment_system.service;

import com.ajitesh.medical_appointment_system.entity.Admin;
import com.ajitesh.medical_appointment_system.repository.AdminRepository;
import com.ajitesh.medical_appointment_system.repository.AppointmentRepository;
import com.ajitesh.medical_appointment_system.repository.DoctorRepository;
import com.ajitesh.medical_appointment_system.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public AdminService(AdminRepository adminRepository,
                        PatientRepository patientRepository,
                        DoctorRepository doctorRepository,
                        AppointmentRepository appointmentRepository) {
        this.adminRepository = adminRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public Optional<Admin> login(String email, String password) {
        return adminRepository.findByEmail(email)
                .filter(a -> a.getPasswordHash().equals(simpleHash(password)));
    }

    public Map<String, Long> getDashboardStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalPatients", patientRepository.count());
        stats.put("totalDoctors", doctorRepository.count());
        stats.put("totalAppointments", appointmentRepository.count());
        long bookedToday = appointmentRepository.findAll().stream()
                .filter(a -> a.getAppointmentDate().equals(java.time.LocalDate.now()))
                .count();
        stats.put("appointmentsToday", bookedToday);
        return stats;
    }

    private String simpleHash(String input) {
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
