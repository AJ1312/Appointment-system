package com.ajitesh.medical_appointment_system.dto;

public class QueueStatusResponse {
    private Integer queueId;
    private Integer appointmentId;
    private Integer tokenNumber;
    private Integer queuePosition;
    private Integer estimatedWaitMinutes;
    private String queueStatus;
    private String doctorName;
    private String patientName;

    // Getters and Setters
    public Integer getQueueId() { return queueId; }
    public void setQueueId(Integer queueId) { this.queueId = queueId; }

    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }

    public Integer getTokenNumber() { return tokenNumber; }
    public void setTokenNumber(Integer tokenNumber) { this.tokenNumber = tokenNumber; }

    public Integer getQueuePosition() { return queuePosition; }
    public void setQueuePosition(Integer queuePosition) { this.queuePosition = queuePosition; }

    public Integer getEstimatedWaitMinutes() { return estimatedWaitMinutes; }
    public void setEstimatedWaitMinutes(Integer estimatedWaitMinutes) { this.estimatedWaitMinutes = estimatedWaitMinutes; }

    public String getQueueStatus() { return queueStatus; }
    public void setQueueStatus(String queueStatus) { this.queueStatus = queueStatus; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
}
