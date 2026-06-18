package com.ajitesh.medical_appointment_system.service;

import com.ajitesh.medical_appointment_system.dto.DoctorDTO;
import com.ajitesh.medical_appointment_system.entity.Doctor;
import com.ajitesh.medical_appointment_system.entity.Specialization;
import com.ajitesh.medical_appointment_system.repository.DoctorRepository;
import com.ajitesh.medical_appointment_system.repository.SpecializationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final SpecializationRepository specializationRepository;

    public DoctorService(DoctorRepository doctorRepository, SpecializationRepository specializationRepository) {
        this.doctorRepository = doctorRepository;
        this.specializationRepository = specializationRepository;
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
        return dto;
    }

    // AI Feature: Doctor Recommendation by symptom keywords
    public List<DoctorDTO> recommendDoctors(String symptoms) {
        String lower = symptoms.toLowerCase();
        String specializationKeyword = mapSymptomsToSpecialization(lower);
        return doctorRepository.findBySpecialization_NameContainingIgnoreCase(specializationKeyword)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private String mapSymptomsToSpecialization(String symptoms) {
        if (symptoms.contains("heart") || symptoms.contains("chest") || symptoms.contains("cardiac"))
            return "Cardiology";
        if (symptoms.contains("brain") || symptoms.contains("headache") || symptoms.contains("neuro") || symptoms.contains("seizure"))
            return "Neurology";
        if (symptoms.contains("bone") || symptoms.contains("joint") || symptoms.contains("fracture") || symptoms.contains("ortho"))
            return "Orthopedics";
        if (symptoms.contains("skin") || symptoms.contains("rash") || symptoms.contains("acne") || symptoms.contains("derma"))
            return "Dermatology";
        if (symptoms.contains("eye") || symptoms.contains("vision") || symptoms.contains("ophthal"))
            return "Ophthalmology";
        if (symptoms.contains("child") || symptoms.contains("pediatric") || symptoms.contains("infant"))
            return "Pediatrics";
        if (symptoms.contains("teeth") || symptoms.contains("dental") || symptoms.contains("tooth") || symptoms.contains("gum"))
            return "Dentistry";
        if (symptoms.contains("stomach") || symptoms.contains("gastro") || symptoms.contains("digestion") || symptoms.contains("abdomen"))
            return "Gastroenterology";
        if (symptoms.contains("urology") || symptoms.contains("kidney") || symptoms.contains("bladder"))
            return "Urology";
        return "General";
    }
}
