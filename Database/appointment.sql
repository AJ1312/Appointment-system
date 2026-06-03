CREATE TABLE appointment (
    appointment_id SERIAL PRIMARY KEY,

    patient_id INT NOT NULL,

    doctor_id INT NOT NULL,

    appointment_date DATE NOT NULL,

    status VARCHAR(20) NOT NULL DEFAULT 'BOOKED',

    reason TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_appointment_patient
        FOREIGN KEY (patient_id)
        REFERENCES patient(patient_id),

    CONSTRAINT fk_appointment_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES doctor(doctor_id)
);