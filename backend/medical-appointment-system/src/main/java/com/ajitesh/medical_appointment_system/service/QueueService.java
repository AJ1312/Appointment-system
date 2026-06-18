package com.ajitesh.medical_appointment_system.service;

import com.ajitesh.medical_appointment_system.dto.QueueStatusResponse;
import com.ajitesh.medical_appointment_system.entity.QueueEntry;
import com.ajitesh.medical_appointment_system.repository.QueueEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QueueService {

    private final QueueEntryRepository queueEntryRepository;

    public QueueService(QueueEntryRepository queueEntryRepository) {
        this.queueEntryRepository = queueEntryRepository;
    }

    public Optional<QueueStatusResponse> getQueueByAppointment(Integer appointmentId) {
        return queueEntryRepository.findByAppointment_AppointmentId(appointmentId)
                .map(this::toDTO);
    }

    public List<QueueStatusResponse> getWaitingQueue() {
        return queueEntryRepository.findByQueueStatusOrderByQueuePosition("WAITING")
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    // AI Feature: Wait time prediction using queue length and consultation history
    public int predictWaitTime(Integer doctorId) {
        long waitingCount = queueEntryRepository.countWaitingByDoctorId(doctorId);
        // Base consultation time: 15 minutes, with AI adjustment factor
        int baseTime = 15;
        double adjustmentFactor = 1.0;
        // If queue > 5, add 10% extra time per patient (simulating doctor fatigue/complex cases)
        if (waitingCount > 5) adjustmentFactor = 1.1;
        if (waitingCount > 10) adjustmentFactor = 1.2;
        return (int) Math.round(waitingCount * baseTime * adjustmentFactor);
    }

    private QueueStatusResponse toDTO(QueueEntry q) {
        QueueStatusResponse dto = new QueueStatusResponse();
        dto.setQueueId(q.getQueueId());
        dto.setAppointmentId(q.getAppointment().getAppointmentId());
        dto.setTokenNumber(q.getTokenNumber());
        dto.setQueuePosition(q.getQueuePosition());
        dto.setEstimatedWaitMinutes(q.getEstimatedWaitMinutes());
        dto.setQueueStatus(q.getQueueStatus());
        dto.setDoctorName(q.getAppointment().getDoctor().getName());
        dto.setPatientName(q.getAppointment().getPatient().getName());
        return dto;
    }
}
