## Specialization Table

Purpose:
Stores medical specializations available in the system.

Primary Key:
- specialization_id

Relationships:
- One specialization can have many doctors.

## Doctor Table

Purpose:
Stores doctor information.

Primary Key:
- doctor_id

Foreign Keys:
- specialization_id -> specialization.specialization_id

Relationship:
One specialization can have many doctors.

## Patient Table

Purpose:
Stores patient account information.

Primary Key:
- patient_id

Constraints:
- email must be unique

Audit Fields:
- created_at

Security:
- passwords are stored as hashes, not plain text.

## Appointment Table

Purpose:
Stores appointment bookings between patients and doctors.

Primary Key:
- appointment_id

Foreign Keys:
- patient_id -> patient.patient_id
- doctor_id -> doctor.doctor_id

Relationships:
- One patient can have many appointments.
- One doctor can have many appointments.

## Relationships

1. Specialization → Doctor (1:N)

2. Doctor → Appointment (1:N)

3. Patient → Appointment (1:N)

4. Appointment → Queue Entry (1:1)

TO INSPECT RELATIONSHIP
SELECT
    tc.table_name,
    kcu.column_name,
    ccu.table_name AS foreign_table_name,
    ccu.column_name AS foreign_column_name
FROM information_schema.table_constraints AS tc
JOIN information_schema.key_column_usage AS kcu
    ON tc.constraint_name = kcu.constraint_name
JOIN information_schema.constraint_column_usage AS ccu
    ON ccu.constraint_name = tc.constraint_name
WHERE tc.constraint_type = 'FOREIGN KEY';