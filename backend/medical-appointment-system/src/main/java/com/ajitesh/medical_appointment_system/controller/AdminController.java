package com.ajitesh.medical_appointment_system.controller;

import com.ajitesh.medical_appointment_system.dto.ApiResponse;
import com.ajitesh.medical_appointment_system.dto.LoginRequest;
import com.ajitesh.medical_appointment_system.dto.LoginResponse;
import com.ajitesh.medical_appointment_system.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        return adminService.login(request.getEmail(), request.getPassword())
                .map(a -> ResponseEntity.ok(ApiResponse.success("Admin login successful",
                        new LoginResponse("Login successful", "ADMIN", a.getAdminId(), a.getName()))))
                .orElse(ResponseEntity.status(401).body(ApiResponse.error("Invalid admin credentials")));
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getDashboardStats() {
        return ResponseEntity.ok(ApiResponse.success("Dashboard stats", adminService.getDashboardStats()));
    }
}
