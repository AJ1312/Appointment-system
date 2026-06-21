package com.ajitesh.medical_appointment_system.dto;

public class DoctorDTO {
    private Integer doctorId;
    private String name;
    private String email;
    private String phone;
    private String qualification;
    private Integer experienceYears;
    private Integer consultationDuration;
    private String specializationName;
    private Integer specializationId;
    private java.util.List<AvailabilitySlot> availableSlots;
    private String earliestSlot;

    public static class AvailabilitySlot {
        private Integer availabilityId;
        private String date;
        private String time;

        public Integer getAvailabilityId() { return availabilityId; }
        public void setAvailabilityId(Integer availabilityId) { this.availabilityId = availabilityId; }

        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }

        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }
    }

    // Getters and Setters
    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    public Integer getExperienceYears() { return experienceYears; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }

    public Integer getConsultationDuration() { return consultationDuration; }
    public void setConsultationDuration(Integer consultationDuration) { this.consultationDuration = consultationDuration; }

    public String getSpecializationName() { return specializationName; }
    public void setSpecializationName(String specializationName) { this.specializationName = specializationName; }

    public Integer getSpecializationId() { return specializationId; }
    public void setSpecializationId(Integer specializationId) { this.specializationId = specializationId; }

    public java.util.List<AvailabilitySlot> getAvailableSlots() { return availableSlots; }
    public void setAvailableSlots(java.util.List<AvailabilitySlot> availableSlots) { this.availableSlots = availableSlots; }

    public String getEarliestSlot() { return earliestSlot; }
    public void setEarliestSlot(String earliestSlot) { this.earliestSlot = earliestSlot; }
}
