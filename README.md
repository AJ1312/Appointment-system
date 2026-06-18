# AI-Assisted Medical Appointment and Queue Management System
## README

---

## 🏥 Project Overview

A full-stack web application built during internship at Hindalco for managing medical appointments, patient queues, and doctor assignments — enhanced with AI features for wait-time prediction and doctor recommendation.

**Tech Stack:**
- **Backend:** Spring Boot 4.0, Java 21, Spring Data JPA, PostgreSQL
- **Frontend:** HTML5, CSS3 (Vanilla, dark theme), JavaScript (ES6+)
- **Database:** PostgreSQL (3NF normalized)
- **AI Features:** Rule-based NLP (symptom → specialization), Queue-length based wait prediction
- **Version Control:** Git / GitHub

---

## 📁 Project Structure

```
Medical-Appointment-Hindalco/
├── backend/
│   └── medical-appointment-system/
│       ├── src/main/java/com/ajitesh/medical_appointment_system/
│       │   ├── entity/          # JPA Entities (Doctor, Patient, Appointment, QueueEntry, Admin, Specialization)
│       │   ├── repository/      # Spring Data JPA Repositories
│       │   ├── service/         # Business Logic Services
│       │   ├── controller/      # REST API Controllers
│       │   ├── dto/             # Data Transfer Objects
│       │   └── config/          # CORS + Exception Handler
│       └── src/main/resources/
│           └── application.properties
├── frontend/
│   ├── index.html       # Landing page with AI demo
│   ├── login.html       # Login (Patient + Admin)
│   ├── register.html    # Patient Registration
│   ├── dashboard.html   # Patient Dashboard
│   ├── admin.html       # Admin Dashboard
│   ├── doctors.html     # Doctor Listing
│   ├── queue.html       # Queue Status + AI Prediction
│   ├── styles.css       # Premium dark design system
│   └── app.js           # Shared JS utilities
├── Database/
│   ├── schema.sql       # Full schema + seed data
│   ├── doctor.sql
│   ├── patient.sql
│   ├── appointment.sql
│   └── queue.sql
├── docs/
│   ├── requirement.md   # SRS
│   ├── scope.md         # Scope Document
│   ├── database_design.md
│   ├── api.md           # API Documentation
│   ├── architecture.md  # Architecture Overview
│   └── logs.md          # Daily Dev Log
├── arch_diag/           # Architecture diagram images
└── README.md
```

---

## ⚡ Quick Start

### Prerequisites
- JDK 21
- Maven
- PostgreSQL (running on localhost:5432)
- Any modern browser

### 1. Database Setup
```sql
-- Connect to PostgreSQL and run:
\i Database/schema.sql
```

### 2. Backend Setup
```bash
cd backend/medical-appointment-system
./mvnw spring-boot:run
```
Backend runs at: `http://localhost:8080`

### 3. Frontend
Open `frontend/index.html` in any browser.
*(No build step required — pure HTML/CSS/JS)*

---

## 🔑 Demo Credentials

| Role   | Email                  | Password  |
|--------|------------------------|-----------|
| Admin  | admin@medicare.com     | admin123  |
| Patient| Register a new account | Any       |

---

## 🌐 API Endpoints

| Method | Endpoint                          | Description                    |
|--------|-----------------------------------|--------------------------------|
| POST   | /api/patients/register            | Register patient                |
| POST   | /api/patients/login               | Patient login                   |
| GET    | /api/patients                     | List all patients               |
| GET    | /api/doctors                      | List all doctors                |
| GET    | /api/doctors/recommend?symptoms=  | AI doctor recommendation        |
| GET    | /api/specializations              | List specializations            |
| POST   | /api/appointments                 | Book appointment + token        |
| GET    | /api/appointments/patient/{id}    | Patient's appointments          |
| PUT    | /api/appointments/{id}/status     | Update appointment status       |
| GET    | /api/queue/appointment/{id}       | Queue status by appointment     |
| GET    | /api/queue/waiting                | All waiting queue entries       |
| GET    | /api/queue/predict-wait/{docId}   | AI wait time prediction         |
| POST   | /api/admin/login                  | Admin login                     |
| GET    | /api/admin/dashboard/stats        | Admin dashboard stats           |

---

## 🤖 AI Features

### 1. Wait Time Prediction
- **Endpoint:** `GET /api/queue/predict-wait/{doctorId}`
- **Logic:** Uses current queue length × consultation duration with an adjustment factor for doctor fatigue at high volumes (>5 or >10 patients)
- **Formula:** `predictedWait = queueLength × baseTime × adjustmentFactor`

### 2. Doctor Recommendation (NLP)
- **Endpoint:** `GET /api/doctors/recommend?symptoms=chest+pain`
- **Logic:** Keyword-based symptom-to-specialization mapping
- **Mapping Examples:**
  - "chest pain, heart" → Cardiology
  - "headache, brain, seizure" → Neurology
  - "bone, joint, fracture" → Orthopedics
  - "skin, rash, acne" → Dermatology
  - "child, infant, pediatric" → Pediatrics

---

## 📐 Architecture

```
[Browser] → [Frontend HTML/JS] → [Spring REST API :8080]
                                         ↓
                                  [Service Layer]
                                  ↙     ↓     ↘
                           [Repository Layer (JPA)]
                                        ↓
                                 [PostgreSQL DB]
```

---

## 🗃️ Database Schema (3NF)

```
specialization(specialization_id, name, description)
doctor(doctor_id, specialization_id FK, name, email, phone, qualification, experience_years, consultation_duration)
patient(patient_id, name, email, phone, password_hash, date_of_birth, gender, created_at)
admin(admin_id, name, email, password_hash)
appointment(appointment_id, patient_id FK, doctor_id FK, appointment_date, status, reason, created_at)
queue_entry(queue_id, appointment_id FK 1:1, token_number, queue_position, estimated_wait_minutes, actual_wait_minutes, queue_status, created_at)
```

---

## 🔄 Application Flow

```
Patient Registration
       ↓
    Login
       ↓
 Book Appointment (Doctor + Date + Reason)
       ↓
Auto Token Generation + Queue Entry
       ↓
 Queue Position & Wait Time Calculated
       ↓
  Admin Updates Status (COMPLETED/CANCELLED)
       ↓
 Patient Views History
```

---

## 🧪 Testing

See `docs/testing_report.md` for full test cases. Quick test:

```bash
# Test patient registration
curl -X POST http://localhost:8080/api/patients/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@test.com","password":"test123","phone":"9999999999"}'

# Test doctor list
curl http://localhost:8080/api/doctors

# Test AI recommendation
curl "http://localhost:8080/api/doctors/recommend?symptoms=chest+pain"
```

---

## 👨‍💻 Author

**Ajitesh Sharma**
Internship Project – Hindalco | 2026
Software Engineering · AI-Assisted Healthcare Systems

---

## 📜 License

This project is for educational/internship purposes.
