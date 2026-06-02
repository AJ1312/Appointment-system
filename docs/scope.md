# Software Requirements Specification (SRS)

## Functional Requirements

### User Management

FR-1: The system shall allow patients to register an account.

FR-2: The system shall allow patients to log in and log out securely.

FR-3: The system shall allow doctors to log in and access their dashboard.

FR-4: The system shall allow administrators to log in and access administrative functions.

### Patient Management

FR-5: Patients shall be able to view and update their profile information.

FR-6: Patients shall be able to browse doctors by specialization.

FR-7: Patients shall be able to view doctor details.

### Appointment Management

FR-8: Patients shall be able to book appointments with available doctors.

FR-9: Patients shall be able to cancel appointments.

FR-10: The system shall store appointment details in the database.

FR-11: Doctors shall be able to view their scheduled appointments.

FR-12: Doctors shall be able to update appointment status.

### Queue Management

FR-13: The system shall automatically generate a queue token for each appointment.

FR-14: The system shall maintain the order of patients in the queue.

FR-15: Patients shall be able to view their queue position.

FR-16: The system shall calculate estimated waiting times.

### Administration

FR-17: Administrators shall be able to add, update, and remove doctor records.

FR-18: Administrators shall be able to manage specializations.

FR-19: Administrators shall be able to monitor appointments.

### Notification Management

FR-20: The system shall send email confirmations after successful appointment booking.

FR-21: The system shall send appointment status notifications to patients.

---

## Non-Functional Requirements

### Performance

NFR-1: The system shall respond to user requests within 3 seconds under normal operating conditions.

NFR-2: The system shall support multiple concurrent users.

### Security

NFR-3: User passwords shall be stored using secure hashing mechanisms.

NFR-4: Only authenticated users shall access protected resources.

NFR-5: Role-based authorization shall be implemented.

### Reliability

NFR-6: The system shall maintain data consistency and integrity.

NFR-7: Appointment information shall not be lost during normal operation.

### Availability

NFR-8: The system shall be available 24 hours a day except during scheduled maintenance.

### Usability

NFR-9: The system shall provide a user-friendly interface.

NFR-10: The application shall support modern web browsers.

### Maintainability

NFR-11: The application shall follow a modular architecture to facilitate maintenance and future enhancements.

NFR-12: Source code shall be maintained through Git version control.

### Scalability

NFR-13: The system architecture shall support future integration of AI-based queue prediction and doctor recommendation modules.
