package com.ajitesh.medical_appointment_system.controller;

import com.ajitesh.medical_appointment_system.dto.ApiResponse;
import com.ajitesh.medical_appointment_system.dto.QueueStatusResponse;
import com.ajitesh.medical_appointment_system.service.QueueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queue")
@CrossOrigin(origins = "*")
public class QueueController {

    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<ApiResponse<QueueStatusResponse>> getQueueStatus(@PathVariable Integer appointmentId) {
        return queueService.getQueueByAppointment(appointmentId)
                .map(q -> ResponseEntity.ok(ApiResponse.success("Queue status", q)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/waiting")
    public ResponseEntity<ApiResponse<List<QueueStatusResponse>>> getWaitingQueue() {
        return ResponseEntity.ok(ApiResponse.success("Waiting queue", queueService.getWaitingQueue()));
    }

    // AI Feature: Predict wait time
    @GetMapping("/predict-wait/{doctorId}")
    public ResponseEntity<ApiResponse<Integer>> predictWaitTime(@PathVariable Integer doctorId) {
        int wait = queueService.predictWaitTime(doctorId);
        return ResponseEntity.ok(ApiResponse.success("Predicted wait time in minutes", wait));
    }
}
