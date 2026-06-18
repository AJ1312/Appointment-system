# Development Log
## MediCare – AI Medical Appointment System

---

## Day 1 — 2026-06-01

**Completed:**
- Installed JDK 21, IntelliJ IDEA, and PostgreSQL
- Initialised Git repository
- Created project on GitHub

**Next:** Requirements and use case diagram

---

## Day 2 — 2026-06-03

**Completed:**
- Created `requirement.md` with functional and non-functional requirements
- Created `scope.md` defining project boundary and future scope
- Identified primary actors: Patient, Doctor, Admin
- Produced Use Case Diagram (v1)
- Decided on tech stack: Spring Boot + PostgreSQL + Vanilla JS

**Challenges:** Balancing scope for a 2–3 week internship

**Decisions:**
- Use PostgreSQL as the primary database
- Implement core features first, AI features second
- Follow Agile incremental development model

**Commit:** `docs: add requirements, scope, and use case diagram`

---

## Day 3 — 2026-06-05

**Completed:**
- Designed ER diagram for all 6 entities
- Finalised 3NF database schema
- Created SQL files for each table
- Set up PostgreSQL database `postgres` on port 5432
- Created Spring Boot project via Spring Initializr (Java 21, Spring Data JPA, PostgreSQL, Lombok)

**Decisions:**
- Use `SERIAL` for all primary keys
- Store passwords as SHA-256 hashes
- `consultation_duration` drives queue wait time calculation

**Commit:** `feat: initialise Spring Boot project and database schema`

---

## Day 4 — 2026-06-07

**Completed:**
- Implemented `Specialization` entity, repository, service, controller
- Tested `GET /specializations` — returned empty list (no data yet)
- Added seed data SQL script
- Ran schema.sql on PostgreSQL — all tables created successfully

**Issues:** Spring Boot 4.0 uses `spring-boot-starter-webmvc` not `spring-boot-starter-web`

**Commit:** `feat: specialization module — entity, repo, service, controller`

---

## Day 5 — 2026-06-09

**Completed:**
- Implemented complete `Doctor` entity with all fields
- Implemented `Patient` entity with SHA-256 password hashing
- Implemented `Appointment` and `QueueEntry` entities
- Implemented `Admin` entity
- Created all 6 repositories

**Commit:** `feat: all JPA entities and repositories`

---

## Day 6 — 2026-06-11

**Completed:**
- Created all DTOs: PatientDTO, DoctorDTO, AppointmentRequest, AppointmentResponse, QueueStatusResponse, LoginRequest, LoginResponse, ApiResponse (generic wrapper)
- Added `GlobalExceptionHandler` with `@RestControllerAdvice`
- Added `CorsConfig` to allow frontend requests

**Commit:** `feat: DTOs, global exception handler, CORS config`

---

## Day 7 — 2026-06-13

**Completed:**
- Implemented `PatientService` (register, login, list, get by ID)
- Implemented `DoctorService` (CRUD + AI recommendation)
- Implemented `AppointmentService` (booking + token generation + queue entry creation)
- Implemented `QueueService` (status, waiting list + AI wait prediction)
- Implemented `AdminService` (login, dashboard stats)

**Key Design Decision:**
- Token number is max token for that doctor today + 1
- Queue position is count of current WAITING entries + 1
- AI recommendation uses keyword regex matching over 9 specialization categories

**Commit:** `feat: all services with business logic and AI features`

---

## Day 8 — 2026-06-15

**Completed:**
- Implemented all 6 REST controllers (Patient, Doctor, Appointment, Queue, Admin, Specialization)
- Tested all endpoints manually with curl
- Fixed `CURRENT_DATE` JPQL query for today's token counting
- Updated `application.properties` — confirmed connection to PostgreSQL

**API Tests Passed:**
- POST /api/patients/register
- POST /api/patients/login
- GET /api/doctors
- GET /api/doctors/recommend?symptoms=chest+pain
- POST /api/appointments
- GET /api/queue/waiting
- GET /api/queue/predict-wait/1
- GET /api/admin/dashboard/stats

**Commit:** `feat: all REST controllers and complete API layer`

---

## Day 9 — 2026-06-17

**Completed:**
- Built complete frontend in HTML, CSS, JavaScript
- Pages: index (landing), login (patient+admin tabs), register, dashboard (4 sections), admin (6 sections), doctors, queue
- Implemented dark premium design system in `styles.css`
- Connected all pages to the REST API via `app.js`
- Tested patient registration, login, appointment booking, queue tracking end-to-end

**Design Decisions:**
- No frontend framework (pure HTML/JS) as required
- Dark colour scheme with Inter font for readability
- Session stored in localStorage as a stop-gap (production would use JWT)

**Commit:** `feat: complete frontend — all pages connected to REST API`

---

## Day 10 — 2026-06-19

**Completed:**
- Redesigned frontend to remove emojis, adopted minimal elegant typography
- Added inline SVG icons for sidebar navigation
- Completed all documentation: SRS, scope, database design, API docs, architecture, logs
- Created comprehensive README
- Pushed to GitHub: `Medical_appointment_system_Hindalco`

**Commit:** `docs: complete documentation, clean frontend, ready for submission`

---

## Software Engineering Principles Applied

| Day | Concept Applied                          |
|-----|------------------------------------------|
| 2   | Requirements Engineering, Scope Analysis |
| 3   | ER Modelling, 3NF Normalisation          |
| 4–7 | Layered Architecture, SRP, DRY           |
| 8   | RESTful API Design, CORS, Error Handling |
| 9   | Separation of Concerns (Frontend/Backend)|
| 10  | Documentation, Git history, Agile Retro  |
