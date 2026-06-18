# API Documentation
## MediCare – AI Medical Appointment System

Base URL: `http://localhost:8080/api`

All responses follow the standard envelope:
```json
{ "success": true, "message": "...", "data": { ... } }
```

---

## Patient Endpoints

### POST `/patients/register`
Register a new patient.

**Request Body:**
```json
{
  "name": "Ajitesh Sharma",
  "email": "ajitesh@example.com",
  "phone": "9876543210",
  "password": "secret123",
  "gender": "Male",
  "dateOfBirth": "2000-01-15"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Patient registered successfully",
  "data": { "patientId": 1, "name": "Ajitesh Sharma", "email": "..." }
}
```

---

### POST `/patients/login`
Authenticate a patient.

**Request Body:** `{ "email": "...", "password": "..." }`

**Response:** `{ "success": true, "data": { "role": "PATIENT", "userId": 1, "name": "..." } }`

---

### GET `/patients`
List all patients (admin use).

### GET `/patients/{id}`
Get patient by ID.

---

## Doctor Endpoints

### GET `/doctors`
List all doctors.

### GET `/doctors/{id}`
Get a single doctor.

### GET `/doctors/specialization/{specializationId}`
Filter doctors by specialization.

### POST `/doctors`
Add a new doctor (admin only).

**Request Body:**
```json
{
  "name": "Dr. Rajiv Sharma",
  "email": "rajiv@hospital.com",
  "phone": "9876543210",
  "qualification": "MBBS, MD Cardiology",
  "specializationId": 1,
  "experienceYears": 12,
  "consultationDuration": 20
}
```

### DELETE `/doctors/{id}`
Remove a doctor.

### GET `/doctors/recommend?symptoms=chest+pain`
**AI Feature** – Doctor recommendation via symptom-to-specialization NLP.

**Response:**
```json
{
  "success": true,
  "message": "Recommended doctors",
  "data": [ { "doctorId": 1, "name": "Dr. Rajiv Sharma", "specializationName": "Cardiology", ... } ]
}
```

---

## Specialization Endpoints

### GET `/specializations`
List all specializations.

---

## Appointment Endpoints

### POST `/appointments`
Book an appointment. Automatically creates a queue entry and generates a token.

**Request Body:**
```json
{
  "patientId": 1,
  "doctorId": 2,
  "appointmentDate": "2026-06-20",
  "reason": "Recurring headaches and dizziness"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Appointment booked successfully",
  "data": {
    "appointmentId": 5,
    "patientName": "Ajitesh Sharma",
    "doctorName": "Dr. Priya Verma",
    "specialization": "Neurology",
    "appointmentDate": "2026-06-20",
    "status": "BOOKED",
    "tokenNumber": 3,
    "queuePosition": 3,
    "estimatedWaitMinutes": 75
  }
}
```

### GET `/appointments`
List all appointments.

### GET `/appointments/patient/{patientId}`
Get all appointments for a patient.

### GET `/appointments/doctor/{doctorId}`
Get all appointments for a doctor.

### PUT `/appointments/{id}/status?status=COMPLETED`
Update appointment status. Valid values: `BOOKED`, `COMPLETED`, `CANCELLED`.

---

## Queue Endpoints

### GET `/queue/appointment/{appointmentId}`
Get queue entry for a specific appointment.

**Response:**
```json
{
  "success": true,
  "data": {
    "queueId": 5,
    "appointmentId": 5,
    "tokenNumber": 3,
    "queuePosition": 3,
    "estimatedWaitMinutes": 75,
    "queueStatus": "WAITING",
    "doctorName": "Dr. Priya Verma",
    "patientName": "Ajitesh Sharma"
  }
}
```

### GET `/queue/waiting`
List all currently waiting queue entries.

### GET `/queue/predict-wait/{doctorId}`
**AI Feature** – Predict estimated wait time for a doctor's queue.

**Algorithm:**
- `waitTime = waitingCount × baseTime × adjustmentFactor`
- `adjustmentFactor = 1.0` (queue ≤ 5), `1.1` (queue ≤ 10), `1.2` (queue > 10)

---

## Admin Endpoints

### POST `/admin/login`
**Request Body:** `{ "email": "admin@medicare.com", "password": "admin123" }`

### GET `/admin/dashboard/stats`
Returns system-wide stats:
```json
{
  "totalPatients": 12,
  "totalDoctors": 6,
  "totalAppointments": 34,
  "appointmentsToday": 5
}
```

---

## Error Responses

All errors return:
```json
{ "success": false, "message": "Error description", "data": null }
```

| Status | Meaning                  |
|--------|--------------------------|
| 400    | Bad request / validation |
| 401    | Unauthorized             |
| 404    | Resource not found       |
| 500    | Internal server error    |
