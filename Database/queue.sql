CREATE TABLE queue_entry (
    queue_id SERIAL PRIMARY KEY,

    appointment_id INT UNIQUE NOT NULL,

    token_number INT NOT NULL,

    queue_position INT NOT NULL,

    estimated_wait_minutes INT,

    actual_wait_minutes INT,

    queue_status VARCHAR(20) DEFAULT 'WAITING',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_queue_appointment
        FOREIGN KEY (appointment_id)
        REFERENCES appointment(appointment_id)
);