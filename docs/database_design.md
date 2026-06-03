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