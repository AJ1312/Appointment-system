package com.ajitesh.medical_appointment_system.repository;

import com.ajitesh.medical_appointment_system.entity.QueueEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QueueEntryRepository extends JpaRepository<QueueEntry, Integer> {
    Optional<QueueEntry> findByAppointment_AppointmentId(Integer appointmentId);

    @Query("SELECT MAX(q.tokenNumber) FROM QueueEntry q WHERE q.appointment.doctor.doctorId = :doctorId AND q.appointment.appointmentDate = CURRENT_DATE")
    Optional<Integer> findMaxTokenForDoctorToday(Integer doctorId);

    @Query("SELECT COUNT(q) FROM QueueEntry q WHERE q.appointment.doctor.doctorId = :doctorId AND q.queueStatus = 'WAITING'")
    long countWaitingByDoctorId(Integer doctorId);

    List<QueueEntry> findByQueueStatusOrderByQueuePosition(String status);
    List<QueueEntry> findByAppointment_Doctor_DoctorIdAndQueueStatusOrderByQueuePosition(Integer doctorId, String status);
}
