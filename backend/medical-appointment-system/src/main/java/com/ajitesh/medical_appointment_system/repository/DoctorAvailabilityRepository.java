package com.ajitesh.medical_appointment_system.repository;

import com.ajitesh.medical_appointment_system.entity.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Integer> {
    List<DoctorAvailability> findByDoctor_DoctorIdAndAvailableDateOrderByStartTimeAsc(Integer doctorId, LocalDate availableDate);
    List<DoctorAvailability> findByDoctor_DoctorIdAndAvailableDateAndIsBookedOrderByStartTimeAsc(Integer doctorId, LocalDate availableDate, Boolean isBooked);
    
    // Find unbooked slots for a doctor on or after today
    List<DoctorAvailability> findByDoctor_DoctorIdAndAvailableDateGreaterThanEqualAndIsBookedOrderByAvailableDateAscStartTimeAsc(Integer doctorId, LocalDate today, Boolean isBooked);
}
