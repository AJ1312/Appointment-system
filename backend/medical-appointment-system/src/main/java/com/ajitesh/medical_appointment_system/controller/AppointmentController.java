package com.ajitesh.medical_appointment_system.controller;

import com.ajitesh.medical_appointment_system.dto.ApiResponse;
import com.ajitesh.medical_appointment_system.dto.AppointmentRequest;
import com.ajitesh.medical_appointment_system.dto.AppointmentResponse;
import com.ajitesh.medical_appointment_system.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponse>> bookAppointment(@RequestBody AppointmentRequest request) {
        AppointmentResponse response = appointmentService.bookAppointment(request);
        return ResponseEntity.ok(ApiResponse.success("Appointment booked successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAllAppointments() {
        return ResponseEntity.ok(ApiResponse.success("Appointments fetched", appointmentService.getAllAppointments()));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getByPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(ApiResponse.success("Patient appointments",
                appointmentService.getAppointmentsByPatient(patientId)));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getByDoctor(@PathVariable Integer doctorId) {
        return ResponseEntity.ok(ApiResponse.success("Doctor appointments",
                appointmentService.getAppointmentsByDoctor(doctorId)));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<AppointmentResponse>> updateStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.success("Status updated", appointmentService.updateStatus(id, status)));
    }
}
