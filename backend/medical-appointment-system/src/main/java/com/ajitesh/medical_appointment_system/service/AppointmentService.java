package com.ajitesh.medical_appointment_system.service;

import com.ajitesh.medical_appointment_system.dto.AppointmentRequest;
import com.ajitesh.medical_appointment_system.dto.AppointmentResponse;
import com.ajitesh.medical_appointment_system.entity.Appointment;
import com.ajitesh.medical_appointment_system.entity.Doctor;
import com.ajitesh.medical_appointment_system.entity.DoctorAvailability;
import com.ajitesh.medical_appointment_system.entity.Patient;
import com.ajitesh.medical_appointment_system.entity.QueueEntry;
import com.ajitesh.medical_appointment_system.repository.AppointmentRepository;
import com.ajitesh.medical_appointment_system.repository.DoctorAvailabilityRepository;
import com.ajitesh.medical_appointment_system.repository.DoctorRepository;
import com.ajitesh.medical_appointment_system.repository.PatientRepository;
import com.ajitesh.medical_appointment_system.repository.QueueEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final QueueEntryRepository queueEntryRepository;
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;
    private final DoctorService doctorService;

    public AppointmentService(AppointmentRepository appointmentRepository,
                               PatientRepository patientRepository,
                               DoctorRepository doctorRepository,
                               QueueEntryRepository queueEntryRepository,
                               DoctorAvailabilityRepository doctorAvailabilityRepository,
                               DoctorService doctorService) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.queueEntryRepository = queueEntryRepository;
        this.doctorAvailabilityRepository = doctorAvailabilityRepository;
        this.doctorService = doctorService;
    }

    public AppointmentResponse bookAppointment(AppointmentRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found: " + request.getPatientId()));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found: " + request.getDoctorId()));

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setReason(request.getReason());
        appointment.setStatus("BOOKED");

        // Process availability slot if selected
        if (request.getAvailabilityId() != null) {
            DoctorAvailability slot = doctorAvailabilityRepository.findById(request.getAvailabilityId())
                    .orElseThrow(() -> new RuntimeException("Availability slot not found: " + request.getAvailabilityId()));
            if (slot.getIsBooked()) {
                throw new RuntimeException("This slot is already booked.");
            }
            slot.setIsBooked(true);
            doctorAvailabilityRepository.save(slot);
            appointment.setAppointmentTime(slot.getStartTime().toString() + " - " + slot.getEndTime().toString());
        } else {
            appointment.setAppointmentTime("Standard walk-in");
        }

        // Run AI Symptom Triage Analysis
        if (request.getReason() != null) {
            Map<String, String> triage = doctorService.runTriageAnalysis(request.getReason());
            appointment.setTriagePriority(triage.get("priority"));
            appointment.setTriageSpecialization(triage.get("specialization"));
            appointment.setTriageAction(triage.get("action"));
        } else {
            appointment.setTriagePriority("Low");
            appointment.setTriageSpecialization("General Medicine");
            appointment.setTriageAction("Standard booking");
        }

        Appointment saved = appointmentRepository.save(appointment);

        // Auto-generate queue token
        int tokenNumber = generateToken(doctor.getDoctorId());
        long waitingCount = queueEntryRepository.countWaitingByDoctorId(doctor.getDoctorId());
        int queuePos = (int) waitingCount + 1;
        int consultDuration = doctor.getConsultationDuration() != null ? doctor.getConsultationDuration() : 15;
        int estimatedWait = queuePos * consultDuration;

        QueueEntry queueEntry = new QueueEntry();
        queueEntry.setAppointment(saved);
        queueEntry.setTokenNumber(tokenNumber);
        queueEntry.setQueuePosition(queuePos);
        queueEntry.setEstimatedWaitMinutes(estimatedWait);
        queueEntry.setQueueStatus("WAITING");
        queueEntryRepository.save(queueEntry);

        return buildResponse(saved, queueEntry);
    }

    public List<AppointmentResponse> getAppointmentsByPatient(Integer patientId) {
        return appointmentRepository.findByPatient_PatientId(patientId)
                .stream().map(a -> {
                    QueueEntry q = queueEntryRepository
                            .findByAppointment_AppointmentId(a.getAppointmentId()).orElse(null);
                    return buildResponse(a, q);
                }).collect(Collectors.toList());
    }

    public List<AppointmentResponse> getAppointmentsByDoctor(Integer doctorId) {
        return appointmentRepository.findByDoctor_DoctorId(doctorId)
                .stream().map(a -> {
                    QueueEntry q = queueEntryRepository
                            .findByAppointment_AppointmentId(a.getAppointmentId()).orElse(null);
                    return buildResponse(a, q);
                }).collect(Collectors.toList());
    }

    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream().map(a -> {
                    QueueEntry q = queueEntryRepository
                            .findByAppointment_AppointmentId(a.getAppointmentId()).orElse(null);
                    return buildResponse(a, q);
                }).collect(Collectors.toList());
    }

    public AppointmentResponse updateStatus(Integer appointmentId, String status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found: " + appointmentId));
        appointment.setStatus(status);
        appointmentRepository.save(appointment);

        // Update queue status if completed/cancelled
        queueEntryRepository.findByAppointment_AppointmentId(appointmentId).ifPresent(q -> {
            if ("COMPLETED".equals(status)) q.setQueueStatus("COMPLETED");
            if ("CANCELLED".equals(status)) q.setQueueStatus("CANCELLED");
            queueEntryRepository.save(q);

            // Realign remaining waiting queue entries for this doctor
            if ("COMPLETED".equals(status) || "CANCELLED".equals(status)) {
                realignQueueForDoctor(appointment.getDoctor().getDoctorId());
            }
        });

        QueueEntry q = queueEntryRepository.findByAppointment_AppointmentId(appointmentId).orElse(null);
        return buildResponse(appointment, q);
    }

    private void realignQueueForDoctor(Integer doctorId) {
        List<QueueEntry> waitingQueue = queueEntryRepository
                .findByAppointment_Doctor_DoctorIdAndQueueStatusOrderByQueuePosition(doctorId, "WAITING");
        int pos = 1;
        for (QueueEntry entry : waitingQueue) {
            entry.setQueuePosition(pos);
            int consultDuration = entry.getAppointment().getDoctor().getConsultationDuration() != null 
                    ? entry.getAppointment().getDoctor().getConsultationDuration() : 15;
            entry.setEstimatedWaitMinutes(pos * consultDuration);
            queueEntryRepository.save(entry);
            pos++;
        }
    }


    public AppointmentResponse updateDiagnostics(Integer appointmentId, String diagnostics) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found: " + appointmentId));
        appointment.setDiagnostics(diagnostics);
        appointmentRepository.save(appointment);

        QueueEntry q = queueEntryRepository.findByAppointment_AppointmentId(appointmentId).orElse(null);
        return buildResponse(appointment, q);
    }

    private int generateToken(Integer doctorId) {
        Optional<Integer> maxToken = queueEntryRepository.findMaxTokenForDoctorToday(doctorId);
        return maxToken.orElse(0) + 1;
    }

    private AppointmentResponse buildResponse(Appointment a, QueueEntry q) {
        AppointmentResponse resp = new AppointmentResponse();
        resp.setAppointmentId(a.getAppointmentId());
        resp.setPatientName(a.getPatient().getName());
        resp.setDoctorName(a.getDoctor().getName());
        if (a.getDoctor().getSpecialization() != null)
            resp.setSpecialization(a.getDoctor().getSpecialization().getName());
        resp.setAppointmentDate(a.getAppointmentDate());
        resp.setStatus(a.getStatus());
        resp.setReason(a.getReason());
        resp.setDiagnostics(a.getDiagnostics());
        resp.setTriagePriority(a.getTriagePriority());
        resp.setTriageSpecialization(a.getTriageSpecialization());
        resp.setTriageAction(a.getTriageAction());
        resp.setAppointmentTime(a.getAppointmentTime());
        if (q != null) {
            resp.setTokenNumber(q.getTokenNumber());
            resp.setQueuePosition(q.getQueuePosition());
            resp.setEstimatedWaitMinutes(q.getEstimatedWaitMinutes());
        }
        return resp;
    }
}
