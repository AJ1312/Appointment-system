CREATE TABLE doctor ( 
doctor_id SERIAL PRIMARY KEY,
specialization_id INT NOT NULL,
name VARCHAR(100) NOT NULL,
email VARCHAR(100) UNIQUE NOT NULL,
phone VARCHAR(15),

qualification VARCHAR(100),
experience_years INT,
consultation_years INT,

consultation_duration INT NOT NULL,

CONSTRAINT fk_doctor_specialization
FOREIGN KEY (specialization_id)
REFERENCES specialization(specialization_id)
)