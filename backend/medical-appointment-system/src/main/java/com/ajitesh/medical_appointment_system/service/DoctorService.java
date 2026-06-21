package com.ajitesh.medical_appointment_system.service;

import com.ajitesh.medical_appointment_system.dto.DoctorDTO;
import com.ajitesh.medical_appointment_system.entity.Doctor;
import com.ajitesh.medical_appointment_system.entity.DoctorAvailability;
import com.ajitesh.medical_appointment_system.entity.Specialization;
import com.ajitesh.medical_appointment_system.repository.DoctorAvailabilityRepository;
import com.ajitesh.medical_appointment_system.repository.DoctorRepository;
import com.ajitesh.medical_appointment_system.repository.SpecializationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final SpecializationRepository specializationRepository;
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;

    public DoctorService(DoctorRepository doctorRepository, 
                         SpecializationRepository specializationRepository,
                         DoctorAvailabilityRepository doctorAvailabilityRepository) {
        this.doctorRepository = doctorRepository;
        this.specializationRepository = specializationRepository;
        this.doctorAvailabilityRepository = doctorAvailabilityRepository;
    }

    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<Doctor> getDoctorById(Integer id) {
        return doctorRepository.findById(id);
    }

    public List<DoctorDTO> getDoctorsBySpecialization(Integer specializationId) {
        return doctorRepository.findBySpecialization_SpecializationId(specializationId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Doctor addDoctor(DoctorDTO dto) {
        Doctor doctor = new Doctor();
        doctor.setName(dto.getName());
        doctor.setEmail(dto.getEmail());
        doctor.setPhone(dto.getPhone());
        doctor.setQualification(dto.getQualification());
        doctor.setExperienceYears(dto.getExperienceYears());
        doctor.setConsultationDuration(dto.getConsultationDuration() != null ? dto.getConsultationDuration() : 15);
        if (dto.getSpecializationId() != null) {
            specializationRepository.findById(dto.getSpecializationId()).ifPresent(doctor::setSpecialization);
        }
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Integer id) {
        doctorRepository.deleteById(id);
    }

    public Optional<Doctor> login(String email, String password) {
        return doctorRepository.findByEmail(email)
                .filter(d -> d.getPasswordHash().equals(simpleHash(password)));
    }

    // Availability management
    public List<DoctorAvailability> getAvailability(Integer doctorId, LocalDate date) {
        return doctorAvailabilityRepository.findByDoctor_DoctorIdAndAvailableDateOrderByStartTimeAsc(doctorId, date);
    }

    public DoctorAvailability addAvailabilitySlot(Integer doctorId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + doctorId));
        DoctorAvailability slot = new DoctorAvailability();
        slot.setDoctor(doctor);
        slot.setAvailableDate(date);
        slot.setStartTime(startTime);
        slot.setEndTime(endTime);
        slot.setIsBooked(false);
        return doctorAvailabilityRepository.save(slot);
    }

    public void deleteAvailabilitySlot(Integer slotId) {
        doctorAvailabilityRepository.deleteById(slotId);
    }

    public DoctorDTO toDTO(Doctor doctor) {
        DoctorDTO dto = new DoctorDTO();
        dto.setDoctorId(doctor.getDoctorId());
        dto.setName(doctor.getName());
        dto.setEmail(doctor.getEmail());
        dto.setPhone(doctor.getPhone());
        dto.setQualification(doctor.getQualification());
        dto.setExperienceYears(doctor.getExperienceYears());
        dto.setConsultationDuration(doctor.getConsultationDuration());
        if (doctor.getSpecialization() != null) {
            dto.setSpecializationName(doctor.getSpecialization().getName());
            dto.setSpecializationId(doctor.getSpecialization().getSpecializationId());
        }

        // Fetch unbooked availability slots
        LocalDate today = LocalDate.now();
        List<DoctorAvailability> slots = doctorAvailabilityRepository
                .findByDoctor_DoctorIdAndAvailableDateGreaterThanEqualAndIsBookedOrderByAvailableDateAscStartTimeAsc(
                        doctor.getDoctorId(), today, false);
        
        List<DoctorDTO.AvailabilitySlot> dtoList = new ArrayList<>();
        for (DoctorAvailability slot : slots) {
            DoctorDTO.AvailabilitySlot sDto = new DoctorDTO.AvailabilitySlot();
            sDto.setAvailabilityId(slot.getAvailabilityId());
            sDto.setDate(slot.getAvailableDate().toString());
            sDto.setTime(slot.getStartTime().toString() + " - " + slot.getEndTime().toString());
            dtoList.add(sDto);
        }
        dto.setAvailableSlots(dtoList);
        if (!dtoList.isEmpty()) {
            dto.setEarliestSlot(dtoList.get(0).getDate() + " " + dtoList.get(0).getTime());
        } else {
            dto.setEarliestSlot("No available slots");
        }

        return dto;
    }

    // AI Feature: Doctor Recommendation by symptom keywords
    public List<DoctorDTO> recommendDoctors(String symptoms) {
        String lower = symptoms.toLowerCase();
        Map<String, String> triage = runTriageAnalysis(lower);
        String specializationName = triage.get("specialization");
        
        return doctorRepository.findBySpecialization_NameContainingIgnoreCase(specializationName)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    // AI Symptom Triage Assistant
    public Map<String, String> runTriageAnalysis(String symptoms) {
        String lower = symptoms.toLowerCase();
        Map<String, String> triage = new HashMap<>();
        
        if (lower.contains("chest pain") || lower.contains("shortness of breath") || lower.contains("heart") || lower.contains("cardiac")) {
            triage.put("specialization", "Cardiology");
            triage.put("priority", "High");
            triage.put("action", "Book earliest available appointment");
        } else if (lower.contains("seizure") || lower.contains("numbness") || lower.contains("paralysis") || lower.contains("severe headache") || lower.contains("sudden headache") || lower.contains("stroke")) {
            triage.put("specialization", "Neurology");
            triage.put("priority", "High");
            triage.put("action", "Book earliest available appointment");
        } else if (lower.contains("headache") || lower.contains("dizzy") || lower.contains("migraine") || lower.contains("brain")) {
            triage.put("specialization", "Neurology");
            triage.put("priority", "Medium");
            triage.put("action", "Schedule neurology consult");
        } else if (lower.contains("fracture") || lower.contains("broken bone") || lower.contains("bone broken") || lower.contains("dislocation")) {
            triage.put("specialization", "Orthopedics");
            triage.put("priority", "High");
            triage.put("action", "Book earliest available appointment");
        } else if (lower.contains("joint") || lower.contains("ortho") || lower.contains("back pain") || lower.contains("sprain")) {
            triage.put("specialization", "Orthopedics");
            triage.put("priority", "Medium");
            triage.put("action", "Schedule orthopedic consult");
        } else if (lower.contains("skin") || lower.contains("rash") || lower.contains("acne") || lower.contains("derma")) {
            triage.put("specialization", "Dermatology");
            triage.put("priority", "Low");
            triage.put("action", "Schedule normal appointment");
        } else if (lower.contains("child") || lower.contains("pediatric") || lower.contains("infant") || lower.contains("baby")) {
            triage.put("specialization", "Pediatrics");
            triage.put("priority", "Medium");
            triage.put("action", "Schedule appointment soon");
        } else if (lower.contains("eye") || lower.contains("vision") || lower.contains("ophthal")) {
            triage.put("specialization", "Ophthalmology");
            triage.put("priority", "Medium");
            triage.put("action", "Schedule standard vision check");
        } else if (lower.contains("teeth") || lower.contains("dental") || lower.contains("tooth") || lower.contains("gum")) {
            triage.put("specialization", "Dentistry");
            triage.put("priority", "Medium");
            triage.put("action", "Schedule dental visit");
        } else if (lower.contains("stomach") || lower.contains("gastro") || lower.contains("digestion") || lower.contains("abdomen")) {
            triage.put("specialization", "Gastroenterology");
            triage.put("priority", "Medium");
            triage.put("action", "Schedule gastroenterologist checkup");
        } else if (lower.contains("urology") || lower.contains("kidney") || lower.contains("bladder")) {
            triage.put("specialization", "Urology");
            triage.put("priority", "Medium");
            triage.put("action", "Schedule consultation");
        } else {
            triage.put("specialization", "General Medicine");
            triage.put("priority", "Low");
            triage.put("action", "Standard booking");
        }
        
        return triage;
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
