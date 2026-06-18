package com.ajitesh.medical_appointment_system.controller;

import com.ajitesh.medical_appointment_system.dto.ApiResponse;
import com.ajitesh.medical_appointment_system.dto.DoctorDTO;
import com.ajitesh.medical_appointment_system.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DoctorDTO>>> getAllDoctors() {
        return ResponseEntity.ok(ApiResponse.success("Doctors fetched", doctorService.getAllDoctors()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorDTO>> getDoctor(@PathVariable Integer id) {
        return doctorService.getDoctorById(id)
                .map(d -> ResponseEntity.ok(ApiResponse.success("Doctor found", doctorService.toDTO(d))))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/specialization/{specializationId}")
    public ResponseEntity<ApiResponse<List<DoctorDTO>>> getBySpecialization(@PathVariable Integer specializationId) {
        return ResponseEntity.ok(ApiResponse.success("Doctors by specialization",
                doctorService.getDoctorsBySpecialization(specializationId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DoctorDTO>> addDoctor(@RequestBody DoctorDTO dto) {
        var doctor = doctorService.addDoctor(dto);
        return ResponseEntity.ok(ApiResponse.success("Doctor added", doctorService.toDTO(doctor)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteDoctor(@PathVariable Integer id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok(ApiResponse.success("Doctor deleted", "OK"));
    }

    // AI Feature: Doctor Recommendation
    @GetMapping("/recommend")
    public ResponseEntity<ApiResponse<List<DoctorDTO>>> recommendDoctors(@RequestParam String symptoms) {
        return ResponseEntity.ok(ApiResponse.success("Recommended doctors", doctorService.recommendDoctors(symptoms)));
    }
}
