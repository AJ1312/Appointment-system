package com.ajitesh.medical_appointment_system.controller;

import com.ajitesh.medical_appointment_system.dto.ApiResponse;
import com.ajitesh.medical_appointment_system.dto.LoginRequest;
import com.ajitesh.medical_appointment_system.dto.LoginResponse;
import com.ajitesh.medical_appointment_system.dto.PatientDTO;
import com.ajitesh.medical_appointment_system.dto.PatientRegistrationRequest;
import com.ajitesh.medical_appointment_system.entity.Patient;
import com.ajitesh.medical_appointment_system.service.PatientService;
import com.ajitesh.medical_appointment_system.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    private final PatientService patientService;
    private final EmailService emailService;

    public PatientController(PatientService patientService, EmailService emailService) {
        this.patientService = patientService;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<PatientDTO>> register(@RequestBody PatientRegistrationRequest request) {
        Patient patient = patientService.registerPatient(request);
        return ResponseEntity.ok(ApiResponse.success("Patient registered successfully", patientService.toDTO(patient)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        return patientService.login(request.getEmail(), request.getPassword())
                .map(p -> {
                    emailService.sendLoginAlert(p.getEmail(), p.getName(), "Patient");
                    return ResponseEntity.ok(ApiResponse.success("Login successful",
                            new LoginResponse("Login successful", "PATIENT", p.getPatientId(), p.getName())));
                })
                .orElse(ResponseEntity.status(401).body(ApiResponse.error("Invalid credentials")));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PatientDTO>>> getAllPatients() {
        List<PatientDTO> list = patientService.getAllPatients().stream()
                .map(patientService::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Patients fetched", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientDTO>> getPatient(@PathVariable Integer id) {
        return patientService.getPatientById(id)
                .map(p -> ResponseEntity.ok(ApiResponse.success("Patient found", patientService.toDTO(p))))
                .orElse(ResponseEntity.notFound().build());
    }
}
