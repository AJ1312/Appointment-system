CREATE TABLE patient (
    patient_id SERIAL PRIMARY KEY,

    name VARCHAR(100) NOT NULL,

    email VARCHAR(100) UNIQUE NOT NULL,

    phone VARCHAR(15),

    password_hash VARCHAR(255) NOT NULL,

    date_of_birth DATE,

    gender VARCHAR(20),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);