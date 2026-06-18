# System Architecture
## MediCare – AI Medical Appointment System

---

## Architecture Overview

The system follows a classic **3-Tier Layered Architecture** with a clean separation of concerns:

```
┌─────────────────────────────────────────────────────────┐
│                     PRESENTATION TIER                    │
│                                                          │
│   HTML5 + CSS3 + Vanilla JavaScript (No frameworks)      │
│   Pages: index, login, register, dashboard, admin,       │
│           doctors, queue                                  │
└────────────────────────┬────────────────────────────────┘
                         │ HTTP / REST (JSON)
                         │ CORS enabled
┌────────────────────────▼────────────────────────────────┐
│                    APPLICATION TIER                      │
│                  Spring Boot 4.0 (Java 21)               │
│                                                          │
│  Controller Layer   →   Service Layer   →   Repository   │
│  (REST endpoints)       (Business logic)    (JPA/Data)   │
│                                                          │
│  Modules:                                                │
│  - PatientController  / PatientService  / PatientRepo    │
│  - DoctorController   / DoctorService   / DoctorRepo     │
│  - AppointmentController / AppointmentService / ApptRepo │
│  - QueueController    / QueueService    / QueueRepo       │
│  - AdminController    / AdminService    / AdminRepo       │
│  - SpecializationController / Service / Repository        │
│                                                          │
│  Cross-cutting: GlobalExceptionHandler, CorsConfig, DTOs │
└────────────────────────┬────────────────────────────────┘
                         │ Spring Data JPA / Hibernate
                         │ JDBC
┌────────────────────────▼────────────────────────────────┐
│                      DATA TIER                           │
│                    PostgreSQL 15+                         │
│                                                          │
│  Tables: specialization, doctor, patient, admin,         │
│          appointment, queue_entry                         │
│  Schema: 3NF Normalized                                  │
└─────────────────────────────────────────────────────────┘
```

---

## Package Structure (Backend)

```
com.ajitesh.medical_appointment_system
├── entity/
│   ├── Specialization.java    ← JPA entity
│   ├── Doctor.java            ← JPA entity
│   ├── Patient.java           ← JPA entity
│   ├── Appointment.java       ← JPA entity
│   ├── QueueEntry.java        ← JPA entity
│   └── Admin.java             ← JPA entity
├── repository/
│   ├── SpecializationRepository.java  ← extends JpaRepository
│   ├── DoctorRepository.java
│   ├── PatientRepository.java
│   ├── AppointmentRepository.java
│   ├── QueueEntryRepository.java
│   └── AdminRepository.java
├── service/
│   ├── SpecializationService.java     ← business logic
│   ├── DoctorService.java             ← includes AI recommendation
│   ├── PatientService.java
│   ├── AppointmentService.java        ← booking + token generation
│   ├── QueueService.java              ← includes AI wait prediction
│   └── AdminService.java
├── controller/
│   ├── SpecializationController.java  ← REST endpoints
│   ├── DoctorController.java
│   ├── PatientController.java
│   ├── AppointmentController.java
│   ├── QueueController.java
│   └── AdminController.java
├── dto/
│   ├── ApiResponse.java               ← generic response wrapper
│   ├── PatientDTO.java
│   ├── PatientRegistrationRequest.java
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── DoctorDTO.java
│   ├── AppointmentRequest.java
│   ├── AppointmentResponse.java
│   └── QueueStatusResponse.java
└── config/
    ├── GlobalExceptionHandler.java    ← @RestControllerAdvice
    └── CorsConfig.java                ← CORS configuration
```

---

## Database Schema (ER Summary)

```
specialization ─────────── doctor (N:1 via specialization_id)
                                │
                                │ (doctor_id)
                                ▼
patient ─────────────── appointment (M:N resolved as 2×1:N)
  (patient_id)              │
                            │ (appointment_id, 1:1)
                            ▼
                       queue_entry
```

**Relationships:**
- Specialization → Doctor : 1 to Many
- Patient → Appointment   : 1 to Many
- Doctor  → Appointment   : 1 to Many
- Appointment → QueueEntry: 1 to 1

---

## AI Feature Architecture

### Wait Time Prediction
```
GET /api/queue/predict-wait/{doctorId}
         │
         ▼
  QueueController.predictWaitTime()
         │
         ▼
  QueueService.predictWaitTime()
  ┌───────────────────────────────────┐
  │ Input: doctorId                   │
  │ Step 1: Count WAITING entries     │
  │ Step 2: Apply base time (15 min)  │
  │ Step 3: Apply adjustment factor   │
  │   > 5 patients  → × 1.1          │
  │   > 10 patients → × 1.2          │
  │ Output: predicted minutes (int)   │
  └───────────────────────────────────┘
```

### Doctor Recommendation (NLP)
```
GET /api/doctors/recommend?symptoms=chest+pain
         │
         ▼
  DoctorController.recommendDoctors()
         │
         ▼
  DoctorService.recommendDoctors()
  ┌───────────────────────────────────┐
  │ Input: symptoms string            │
  │ Step 1: Lowercase normalisation   │
  │ Step 2: Keyword-regex matching    │
  │   "chest|heart|cardiac" → Cardio  │
  │   "brain|headache|neuro" → Neuro  │
  │   ... (9 specialization mappings) │
  │ Step 3: Query DoctorRepository    │
  │   WHERE spec.name LIKE keyword    │
  │ Output: List<DoctorDTO>           │
  └───────────────────────────────────┘
```

---

## Design Principles Applied

| Principle                         | Application                              |
|-----------------------------------|------------------------------------------|
| Single Responsibility Principle   | Each class has exactly one purpose       |
| Separation of Concerns            | Controller / Service / Repository layers |
| DRY (Don't Repeat Yourself)       | DTOs, ApiResponse wrapper, shared JS     |
| 3NF Database Normalization        | No transitive dependencies in schema     |
| Agile / Incremental Development   | Feature-by-feature delivery              |
| Open/Closed Principle             | Services extendable without modification |
