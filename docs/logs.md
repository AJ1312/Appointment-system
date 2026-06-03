# Day 1

Date: 2026-06-01

Completed:
- Installed JDK 21
- Installed IntelliJ IDEA
- Installed PostgreSQL
- Created repository

Issues:
- None

Next:
- Create requirements and use case diagram

# Day 2

**Date:** 2026-06-03

## Objectives

* Finalize project requirements and scope.
* Create the initial Use Case Diagram.
* Establish core project functionality and actor interactions.

## Work Completed

### Documentation

* Created `requirements.md`.
* Defined functional requirements for Patient, Doctor, and Admin modules.
* Defined non-functional requirements including performance, security, reliability, maintainability, and scalability.
* Created `scope.md`.
* Identified project boundaries and future scope.

### System Analysis

* Identified primary actors:

  * Patient
  * Doctor
  * Admin

* Identified major use cases:

  * Register
  * Login
  * Manage Profile
  * View Doctors
  * Search by Specialization
  * Book Appointment
  * View Appointment History
  * View Queue Status
  * Manage Doctors
  * Manage Patients
  * Manage Specializations
  * Monitor Appointments
  * View Reports

### Design Artifacts

* Created Version 1 of the Use Case Diagram.
* Reviewed relationships between actors and system functionality.
* Identified queue generation and appointment booking as core system workflows.

### Software Engineering Concepts Applied

* Requirements Engineering
* Stakeholder Analysis
* Use Case Modeling
* Scope Definition
* Agile Iterative Development

## Challenges Encountered

* Determining the project scope while balancing implementation complexity and learning objectives.
* Deciding which features should be included in Version 1 and which should be deferred to future iterations.

## Decisions Made

* Use PostgreSQL as the primary database.
* Use Spring Boot as the backend framework.
* Use HTML, CSS, and JavaScript for the frontend.
* Follow Agile Iterative Development.
* Build the core appointment and queue management system first.
* Implement AI features after the core system is functional.

## Deliverables Produced

* requirements.md
* scope.md
* Use Case Diagram (Version 1)

## Git Activity

Suggested Commit:

docs: add requirements, scope and use case diagram

## Next Steps (Day 3)

* Create System Architecture Diagram.
* Finalize ER Diagram.
* Design database schema.
* Begin PostgreSQL setup and database creation.

