package com.ajitesh.medical_appointment_system.controller;

import com.ajitesh.medical_appointment_system.dto.ApiResponse;
import com.ajitesh.medical_appointment_system.dto.AppointmentRequest;
import com.ajitesh.medical_appointment_system.dto.AppointmentResponse;
import com.ajitesh.medical_appointment_system.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PutMapping("/{id}/diagnostics")
    public ResponseEntity<ApiResponse<AppointmentResponse>> updateDiagnostics(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(ApiResponse.success("Diagnostics updated",
                appointmentService.updateDiagnostics(id, body.get("diagnostics"))));
    }

    @PutMapping("/{id}/reschedule")
    public ResponseEntity<ApiResponse<AppointmentResponse>> reschedule(
            @PathVariable Integer id,
            @RequestParam Integer availabilityId) {
        return ResponseEntity.ok(ApiResponse.success("Appointment rescheduled successfully",
                appointmentService.rescheduleAppointment(id, availabilityId)));
    }
}
