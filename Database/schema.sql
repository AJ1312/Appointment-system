-- ============================================================
-- Medical Appointment System - Full Database Schema
-- PostgreSQL | 3NF Normalized
-- ============================================================

-- Create specialization table
CREATE TABLE IF NOT EXISTS specialization (
    specialization_id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT
);

-- Create doctor table
CREATE TABLE IF NOT EXISTS doctor (
    doctor_id SERIAL PRIMARY KEY,
    specialization_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15),
    qualification VARCHAR(100),
    experience_years INT,
    consultation_duration INT NOT NULL DEFAULT 15,
    password_hash VARCHAR(255) NOT NULL DEFAULT 'f348d5628621f3d8f59c8cabda0f8eb0aa7e0514a90be7571020b1336f26c113', -- default: doctor123
    CONSTRAINT fk_doctor_specialization
        FOREIGN KEY (specialization_id) REFERENCES specialization(specialization_id)
);

-- Create patient table
CREATE TABLE IF NOT EXISTS patient (
    patient_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15),
    password_hash VARCHAR(255) NOT NULL,
    date_of_birth DATE,
    gender VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create admin table
CREATE TABLE IF NOT EXISTS admin (
    admin_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);

-- Create appointment table
CREATE TABLE IF NOT EXISTS appointment (
    appointment_id SERIAL PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'BOOKED',
    reason TEXT,
    diagnostics TEXT,
    triage_priority VARCHAR(20),
    triage_specialization VARCHAR(100),
    triage_action VARCHAR(255),
    appointment_time VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_appointment_patient
        FOREIGN KEY (patient_id) REFERENCES patient(patient_id),
    CONSTRAINT fk_appointment_doctor
        FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id)
);

-- Create queue_entry table
CREATE TABLE IF NOT EXISTS queue_entry (
    queue_id SERIAL PRIMARY KEY,
    appointment_id INT UNIQUE NOT NULL,
    token_number INT NOT NULL,
    queue_position INT NOT NULL,
    estimated_wait_minutes INT,
    actual_wait_minutes INT,
    queue_status VARCHAR(20) DEFAULT 'WAITING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_queue_appointment
        FOREIGN KEY (appointment_id) REFERENCES appointment(appointment_id)
);

-- Create doctor_availability table
CREATE TABLE IF NOT EXISTS doctor_availability (
    availability_id SERIAL PRIMARY KEY,
    doctor_id INT NOT NULL,
    available_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_booked BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_availability_doctor
        FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id) ON DELETE CASCADE
);

-- ============================================================
-- Seed Data
-- ============================================================

-- Specializations
INSERT INTO specialization (name, description) VALUES
('Cardiology', 'Heart and cardiovascular system'),
('Neurology', 'Brain, spinal cord, and nervous system'),
('Orthopedics', 'Bones, joints, and musculoskeletal system'),
('Dermatology', 'Skin, hair, and nail disorders'),
('Pediatrics', 'Medical care for children'),
('Ophthalmology', 'Eye care and vision'),
('Dentistry', 'Oral health and dental care'),
('General Medicine', 'General health and primary care'),
('Gastroenterology', 'Digestive system disorders'),
('Urology', 'Urinary tract and reproductive health')
ON CONFLICT DO NOTHING;

-- Admin (password: admin123)
INSERT INTO admin (name, email, password_hash) VALUES
('System Admin', 'admin@medicare.com',
 '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9')
ON CONFLICT DO NOTHING;

-- Sample Doctors (default password: doctor123)
INSERT INTO doctor (specialization_id, name, email, phone, qualification, experience_years, consultation_duration) VALUES
-- Cardiology
(1, 'Dr. Rajiv Sharma', 'rajiv.sharma@medicare.com', '9876543210', 'MBBS, MD Cardiology', 12, 20),
(1, 'Dr. Amit Patel', 'amit.patel@medicare.com', '9876543216', 'MBBS, DM Cardiology', 10, 15),
-- Neurology
(2, 'Dr. Priya Verma', 'priya.verma@medicare.com', '9876543211', 'MBBS, DM Neurology', 8, 25),
(2, 'Dr. Rohan Deshmukh', 'rohan.deshmukh@medicare.com', '9876543217', 'MBBS, MD Neurology', 11, 20),
-- Orthopedics
(3, 'Dr. Suresh Kumar', 'suresh.kumar@medicare.com', '9876543212', 'MBBS, MS Orthopedics', 15, 15),
(3, 'Dr. Vikram Rathore', 'vikram.rathore@medicare.com', '9876543218', 'MBBS, MS Ortho', 9, 20),
-- Dermatology
(4, 'Dr. Anita Gupta', 'anita.gupta@medicare.com', '9876543213', 'MBBS, MD Dermatology', 6, 15),
(4, 'Dr. Sarah Khan', 'sarah.khan@medicare.com', '9876543219', 'MBBS, MD Skin', 7, 15),
-- Pediatrics
(5, 'Dr. Manoj Singh', 'manoj.singh@medicare.com', '9876543214', 'MBBS, MD Pediatrics', 10, 20),
(5, 'Dr. Neha Kapoor', 'neha.kapoor@medicare.com', '9876543220', 'MBBS, DCH Pediatrics', 5, 15),
-- Ophthalmology
(6, 'Dr. Rajesh Mehta', 'rajesh.mehta@medicare.com', '9876543221', 'MBBS, MS Ophthalmology', 14, 15),
(6, 'Dr. Sunita Williams', 'sunita.williams@medicare.com', '9876543222', 'MBBS, DO Eye specialist', 8, 20),
-- Dentistry
(7, 'Dr. Arpan Goel', 'arpan.goel@medicare.com', '9876543223', 'BDS, MDS Oral Surgery', 12, 20),
(7, 'Dr. Shalini Sen', 'shalini.sen@medicare.com', '9876543224', 'BDS Dentistry', 6, 15),
-- General Medicine
(8, 'Dr. Kavita Rao', 'kavita.rao@medicare.com', '9876543215', 'MBBS, MD General', 5, 15),
(8, 'Dr. Alok Tripathi', 'alok.tripathi@medicare.com', '9876543225', 'MBBS General Practitioner', 16, 15),
-- Gastroenterology
(9, 'Dr. Sameer Joshi', 'sameer.joshi@medicare.com', '9876543226', 'MBBS, MD, DM Gastro', 11, 20),
(9, 'Dr. Ritu Nanda', 'ritu.nanda@medicare.com', '9876543227', 'MBBS, MD Gastro', 7, 20),
-- Urology
(10, 'Dr. Tarun Jain', 'tarun.jain@medicare.com', '9876543228', 'MBBS, MS, MCh Urology', 13, 20),
(10, 'Dr. Preeti Sinha', 'preeti.sinha@medicare.com', '9876543229', 'MBBS, MS Urology', 8, 15)
ON CONFLICT DO NOTHING;
