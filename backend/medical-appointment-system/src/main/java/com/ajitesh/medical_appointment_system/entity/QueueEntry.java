package com.ajitesh.medical_appointment_system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "queue_entry")
public class QueueEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "queue_id")
    private Integer queueId;

    @OneToOne
    @JoinColumn(name = "appointment_id", unique = true, nullable = false)
    private Appointment appointment;

    @Column(name = "token_number", nullable = false)
    private Integer tokenNumber;

    @Column(name = "queue_position", nullable = false)
    private Integer queuePosition;

    @Column(name = "estimated_wait_minutes")
    private Integer estimatedWaitMinutes;

    @Column(name = "actual_wait_minutes")
    private Integer actualWaitMinutes;

    @Column(name = "queue_status", length = 20)
    private String queueStatus = "WAITING";

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and Setters
    public Integer getQueueId() { return queueId; }
    public void setQueueId(Integer queueId) { this.queueId = queueId; }

    public Appointment getAppointment() { return appointment; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }

    public Integer getTokenNumber() { return tokenNumber; }
    public void setTokenNumber(Integer tokenNumber) { this.tokenNumber = tokenNumber; }

    public Integer getQueuePosition() { return queuePosition; }
    public void setQueuePosition(Integer queuePosition) { this.queuePosition = queuePosition; }

    public Integer getEstimatedWaitMinutes() { return estimatedWaitMinutes; }
    public void setEstimatedWaitMinutes(Integer estimatedWaitMinutes) { this.estimatedWaitMinutes = estimatedWaitMinutes; }

    public Integer getActualWaitMinutes() { return actualWaitMinutes; }
    public void setActualWaitMinutes(Integer actualWaitMinutes) { this.actualWaitMinutes = actualWaitMinutes; }

    public String getQueueStatus() { return queueStatus; }
    public void setQueueStatus(String queueStatus) { this.queueStatus = queueStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
