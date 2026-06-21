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

    // Prioritize high urgency appointment to position 1 and shift the rest
    public QueueStatusResponse prioritizeQueue(Integer appointmentId) {
        QueueEntry target = queueEntryRepository.findByAppointment_AppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("Queue entry not found for appointment: " + appointmentId));
        
        if (!"WAITING".equals(target.getQueueStatus())) {
            throw new RuntimeException("Only waiting appointments can be prioritized.");
        }
        
        Integer doctorId = target.getAppointment().getDoctor().getDoctorId();
        List<QueueEntry> waitingQueue = queueEntryRepository
                .findByAppointment_Doctor_DoctorIdAndQueueStatusOrderByQueuePosition(doctorId, "WAITING");
        
        // Remove target from its current position in list
        waitingQueue.removeIf(q -> q.getQueueId().equals(target.getQueueId()));
        
        // Place target at position 1
        target.setQueuePosition(1);
        int consultDuration = target.getAppointment().getDoctor().getConsultationDuration() != null 
                ? target.getAppointment().getDoctor().getConsultationDuration() : 15;
        target.setEstimatedWaitMinutes(consultDuration);
        queueEntryRepository.save(target);
        
        // Re-align and update the rest of the queue
        int pos = 2;
        for (QueueEntry other : waitingQueue) {
            other.setQueuePosition(pos);
            other.setEstimatedWaitMinutes(pos * consultDuration);
            queueEntryRepository.save(other);
            pos++;
        }
        
        return toDTO(target);
    }

    // AI Feature: Wait time prediction using queue length and consultation history
    public int predictWaitTime(Integer doctorId) {
        long waitingCount = queueEntryRepository.countWaitingByDoctorId(doctorId);
        int baseTime = 15;
        double adjustmentFactor = 1.0;
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
