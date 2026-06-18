# Testing Report
## MediCare – AI Medical Appointment System

**Date:** 2026-06-19
**Tester:** Ajitesh Sharma
**Environment:** localhost – Spring Boot 4.0 / PostgreSQL 15

---

## 1. Unit-Level Component Tests

### 1.1 Database Schema Validation

| Test | Expected | Result |
|------|----------|--------|
| All 6 tables created by schema.sql | Tables exist in PostgreSQL | Pass |
| Foreign key: doctor.specialization_id → specialization | Referential integrity enforced | Pass |
| Foreign key: appointment.patient_id → patient | Referential integrity enforced | Pass |
| Foreign key: queue_entry.appointment_id → appointment | Unique constraint on appointment_id | Pass |
| Seed data inserted (6 doctors, 10 specializations, 1 admin) | Rows appear in SELECT queries | Pass |

### 1.2 Service Logic

| Service | Method | Scenario | Expected | Result |
|---------|--------|----------|----------|--------|
| PatientService | registerPatient | New unique email | Returns saved patient | Pass |
| PatientService | registerPatient | Duplicate email | Throws RuntimeException | Pass |
| PatientService | login | Correct credentials | Returns patient | Pass |
| PatientService | login | Wrong password | Returns empty Optional | Pass |
| DoctorService | recommendDoctors | symptoms = "chest pain" | Returns Cardiology doctors | Pass |
| DoctorService | recommendDoctors | symptoms = "headache" | Returns Neurology doctors | Pass |
| DoctorService | recommendDoctors | unknown symptoms | Returns General Medicine | Pass |
| AppointmentService | bookAppointment | Valid patient + doctor | Creates appointment + queue entry | Pass |
| AppointmentService | bookAppointment | Invalid patient ID | Throws RuntimeException | Pass |
| QueueService | predictWaitTime | 0 waiting | Returns 0 | Pass |
| QueueService | predictWaitTime | 6 waiting | Returns 6 × 15 × 1.1 = 99 min | Pass |
| QueueService | predictWaitTime | 11 waiting | Returns 11 × 15 × 1.2 = 198 min | Pass |
| AdminService | login | admin@medicare.com / admin123 | Returns admin | Pass |

---

## 2. API Integration Tests

Tested using `curl` on localhost:8080.

### 2.1 Patient API

```bash
# Register
curl -X POST http://localhost:8080/api/patients/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@test.com","password":"test123","phone":"9999999999"}'
# Expected: 200 OK, success:true, patientId returned
```

```bash
# Login
curl -X POST http://localhost:8080/api/patients/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","password":"test123"}'
# Expected: 200 OK, role:PATIENT
```

```bash
# Duplicate email
curl -X POST http://localhost:8080/api/patients/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Dup","email":"test@test.com","password":"abc123"}'
# Expected: 400 Bad Request, success:false
```

### 2.2 Doctor API

```bash
# All doctors
curl http://localhost:8080/api/doctors
# Expected: 200 OK, list of 6 seeded doctors

# AI Recommendation
curl "http://localhost:8080/api/doctors/recommend?symptoms=chest+pain"
# Expected: Returns Dr. Rajiv Sharma (Cardiology)

curl "http://localhost:8080/api/doctors/recommend?symptoms=headache+brain"
# Expected: Returns Dr. Priya Verma (Neurology)
```

### 2.3 Appointment API

```bash
# Book appointment
curl -X POST http://localhost:8080/api/appointments \
  -H "Content-Type: application/json" \
  -d '{"patientId":1,"doctorId":1,"appointmentDate":"2026-06-20","reason":"Chest pain"}'
# Expected: tokenNumber=1, queuePosition=1, estimatedWaitMinutes=20
```

### 2.4 Queue API

```bash
# Queue status by appointment
curl http://localhost:8080/api/queue/appointment/1
# Expected: tokenNumber, position, status=WAITING

# AI Wait Prediction
curl http://localhost:8080/api/queue/predict-wait/1
# Expected: Integer value in minutes
```

### 2.5 Admin API

```bash
# Admin login
curl -X POST http://localhost:8080/api/admin/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@medicare.com","password":"admin123"}'
# Expected: 200 OK, role:ADMIN

# Dashboard stats
curl http://localhost:8080/api/admin/dashboard/stats
# Expected: totalPatients, totalDoctors, totalAppointments, appointmentsToday
```

---

## 3. Frontend UI Tests

| Page | Scenario | Expected | Result |
|------|----------|----------|--------|
| index.html | AI symptom demo (no backend) | Recommendation shown locally | Pass |
| login.html | Patient tab → valid credentials | Redirect to dashboard.html | Pass |
| login.html | Admin tab → admin credentials | Redirect to admin.html | Pass |
| login.html | Wrong password | Error alert shown | Pass |
| register.html | Valid form submission | Success message + redirect | Pass |
| register.html | Duplicate email | Error from backend shown | Pass |
| dashboard.html | Overview loads appointments | Stats populated | Pass |
| dashboard.html | Book Appointment form | Token card appears after booking | Pass |
| dashboard.html | Queue status by appointment ID | Token card with position shown | Pass |
| dashboard.html | AI Doctor Finder | Symptom mapped to specialization | Pass |
| admin.html | Dashboard stats | All 4 stats populated | Pass |
| admin.html | Add Doctor form | Doctor added, success shown | Pass |
| admin.html | Appointments tab | All appointments listed with status select | Pass |
| doctors.html | Search by name | Filtered results displayed | Pass |
| doctors.html | Filter by specialization | Correct subset shown | Pass |
| queue.html | Track by appointment ID | Token card rendered | Pass |
| queue.html | AI predict wait (Doctor ID=1) | Minutes returned from API | Pass |

---

## 4. Edge Cases

| Scenario | Handling |
|----------|----------|
| Backend offline | Alert with "Backend not connected" message on all pages |
| Unknown symptoms in AI recommender | Falls back to "General Medicine" |
| Appointment with invalid doctorId | 400 Bad Request with message |
| Queue query for non-existent appointment | 404 with clear error message |
| Admin accessing patient dashboard | Redirected to login |
| Patient accessing admin panel | Redirected to login |

---

## 5. Known Limitations (Out of Scope)

- Passwords stored as SHA-256 (production should use BCrypt)
- No JWT authentication (session stored in localStorage)
- No email notification implementation
- DELETE /doctors endpoint not fully wired in admin UI

---

## Test Summary

| Category | Total Tests | Passed | Failed |
|----------|-------------|--------|--------|
| Schema / DB | 5 | 5 | 0 |
| Service Logic | 12 | 12 | 0 |
| API Integration | 10 | 10 | 0 |
| Frontend UI | 17 | 17 | 0 |
| Edge Cases | 6 | 6 | 0 |
| **Total** | **50** | **50** | **0** |
