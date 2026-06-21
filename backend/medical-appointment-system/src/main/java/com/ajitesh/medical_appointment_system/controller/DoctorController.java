package com.ajitesh.medical_appointment_system.controller;

import com.ajitesh.medical_appointment_system.dto.ApiResponse;
import com.ajitesh.medical_appointment_system.dto.DoctorDTO;
import com.ajitesh.medical_appointment_system.dto.LoginRequest;
import com.ajitesh.medical_appointment_system.dto.LoginResponse;
import com.ajitesh.medical_appointment_system.entity.DoctorAvailability;
import com.ajitesh.medical_appointment_system.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

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

    // AI Feature: Symptom Triage Analysis
    @GetMapping("/triage")
    public ResponseEntity<ApiResponse<Map<String, String>>> runTriage(@RequestParam String symptoms) {
        return ResponseEntity.ok(ApiResponse.success("Symptom triage results", doctorService.runTriageAnalysis(symptoms)));
    }

    // Doctor login endpoint
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        return doctorService.login(request.getEmail(), request.getPassword())
                .map(d -> ResponseEntity.ok(ApiResponse.success("Doctor login successful",
                        new LoginResponse("Login successful", "DOCTOR", d.getDoctorId(), d.getName()))))
                .orElse(ResponseEntity.status(401).body(ApiResponse.error("Invalid doctor credentials")));
    }

    // Availability management endpoints
    @GetMapping("/{id}/availability")
    public ResponseEntity<ApiResponse<List<DoctorAvailability>>> getAvailability(
            @PathVariable Integer id,
            @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(ApiResponse.success("Doctor availability slots", doctorService.getAvailability(id, localDate)));
    }

    @PostMapping("/{id}/availability")
    public ResponseEntity<ApiResponse<DoctorAvailability>> addAvailability(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {
        LocalDate date = LocalDate.parse(body.get("date"));
        LocalTime startTime = LocalTime.parse(body.get("startTime"));
        LocalTime endTime = LocalTime.parse(body.get("endTime"));
        
        DoctorAvailability slot = doctorService.addAvailabilitySlot(id, date, startTime, endTime);
        return ResponseEntity.ok(ApiResponse.success("Availability slot added", slot));
    }

    @DeleteMapping("/availability/{slotId}")
    public ResponseEntity<ApiResponse<String>> deleteAvailability(@PathVariable Integer slotId) {
        doctorService.deleteAvailabilitySlot(slotId);
        return ResponseEntity.ok(ApiResponse.success("Availability slot deleted", "OK"));
    }
}
