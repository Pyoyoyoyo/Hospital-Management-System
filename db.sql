CREATE TABLE hospital (
  id SERIAL PRIMARY KEY,           -- код
  name VARCHAR,                    -- нэр
  location VARCHAR                 -- байршил
);

CREATE TABLE person (
  id SERIAL PRIMARY KEY,           -- код
  registration_number VARCHAR,      -- бүртгэлийн_дугаар
  name VARCHAR,                    -- нэр
  address VARCHAR,                  -- хаяг
  contact_number VARCHAR,           -- холбоо_барих_дугаар
  email VARCHAR,                    -- имэйл
  language_support VARCHAR,         -- хэлний_дэмжлэг
  role VARCHAR                      -- төрөл
);

CREATE TABLE doctor (
  id SERIAL PRIMARY KEY,           -- код
  specialization VARCHAR,           -- мэргэжил
  doctor_id INTEGER,                -- эмч_код
  person_id INTEGER REFERENCES person(id),  -- хүн_код
  hospital_id INTEGER REFERENCES hospital(id) -- эмнэлэг_код
);

CREATE TABLE patient (
  id SERIAL PRIMARY KEY,           -- код
  medical_history VARCHAR,         -- эмнэлгийн_түүх
  patient_id INTEGER,              -- өвчтөн_код
  insurance_information VARCHAR,   -- даатгалын_мэдээлэл
  person_id INTEGER REFERENCES person(id),  -- хүн_код
  hospital_id INTEGER REFERENCES hospital(id) -- эмнэлэг_код
);

CREATE TABLE admin (
  id SERIAL PRIMARY KEY,           -- код
  admin_id INTEGER,                -- админ_код
  person_id INTEGER REFERENCES person(id),  -- хүн_код
  hospital_id INTEGER REFERENCES hospital(id) -- эмнэлэг_код
);

CREATE TABLE nurse (
  id SERIAL PRIMARY KEY,           -- код
  nurse_id INTEGER,                -- сувилагч_код
  professional_level VARCHAR,      -- мэргэжлийн_түвшин
  person_id INTEGER REFERENCES person(id),  -- хүн_код
  hospital_id INTEGER REFERENCES hospital(id) -- эмнэлэг_код
);

CREATE TABLE appointment_record (
  id SERIAL PRIMARY KEY,           -- код
  appointment_code INTEGER,        -- цагийн_код
  date TIMESTAMP,                  -- өдөр
  time VARCHAR,                    -- цаг
  status VARCHAR,                  -- төлөв
  doctor_id INTEGER REFERENCES doctor(id),   -- эмч_код
  patient_id INTEGER REFERENCES patient(id)  -- өвчтөн_код
);

CREATE TABLE medical_history (
  id SERIAL PRIMARY KEY,           -- код
  record_code INTEGER,             -- бичлэгийн_код
  diagnosis VARCHAR,               -- онош
  prescription VARCHAR,            -- жор
  notes VARCHAR,                   -- тэмдэглэл
  doctor_id INTEGER REFERENCES doctor(id),   -- эмч_код
  patient_id INTEGER REFERENCES patient(id)  -- өвчтөн_код
);

CREATE TABLE payment (
  id SERIAL PRIMARY KEY,           -- код
  payment_code INTEGER,            -- төлбөрийн_код
  amount FLOAT,                    -- хэмжээ
  date TIMESTAMP,                  -- огноо
  status VARCHAR,                  -- төлөв
  patient_id INTEGER REFERENCES patient(id) -- өвчтөн_код
);

INSERT INTO person (registration_number, name, address, contact_number, email, language_support, role) VALUES
('PN001', 'John Doe', '123 Main Street, Ulaanbaatar', '99123456', 'john.doe@example.com', 'English', 'Doctor'),
('PN002', 'Jane Smith', '456 Elm Street, Ulaanbaatar', '99234567', 'jane.smith@example.com', 'Mongolian, English', 'Patient'),
('PN003', 'Tsevelmaa Baatar', '789 Pine Road, Ulaanbaatar', '99345678', 'tsevelmaa.b@example.com', 'Mongolian', 'Nurse'),
('PN004', 'Lkhagvasuren Amartuvshin', '1010 Mountain Road, Darkhan', '99456789', 'lkhagvasuren.a@example.com', 'Mongolian', 'Admin'),
('PN005', 'Suren Munkhbat', '1011 Green Street, Ulaanbaatar', '99567890', 'suren.m@example.com', 'Mongolian', 'Nurse');

INSERT INTO hospital (name, location) VALUES
('City Hospital', 'Ulaanbaatar, Mongolia'),
('Central Medical Center', 'Ulaanbaatar, Mongolia'),
('Healthy Life Clinic', 'Ulaanbaatar, Mongolia'),
('Spring Valley Hospital', 'Darkhan, Mongolia');

INSERT INTO doctor (specialization, doctor_id, person_id, hospital_id) VALUES
('Cardiologist', 101, 1, 1),
('Neurologist', 102, 2, 2),
('Pediatrician', 103, 3, 3),
('Orthopedic Surgeon', 104, 4, 4);

INSERT INTO admin (admin_id, person_id, hospital_id) VALUES
(301, 4, 1),
(302, 4, 2),
(303, 4, 3),
(304, 4, 4);

INSERT INTO nurse (nurse_id, professional_level, person_id, hospital_id) VALUES
(401, 'Senior', 3, 1),
(402, 'Junior', 4, 2),
(403, 'Mid-level', 5, 3);

INSERT INTO patient (medical_history, patient_id, insurance_information, person_id, hospital_id) VALUES
('No major illnesses', 201, 'Standard Health Insurance', 2, 1),
('Asthma', 202, 'Premium Health Insurance', 3, 2),
('Chronic back pain', 203, 'Standard Health Insurance', 4, 3),
('Diabetes', 204, 'Premium Health Insurance', 5, 4);

INSERT INTO appointment_record (appointment_code, date, time, status, doctor_id, patient_id) VALUES
(501, '2024-12-05 09:00:00', '09:00', 'active', 5, 21),
(502, '2024-12-05 10:00:00', '10:00', 'active', 6, 22),
(503, '2024-12-05 11:00:00', '11:00', 'active', 7, 23),
(504, '2024-12-05 12:00:00', '12:00', 'active', 8, 24);

INSERT INTO medical_history (record_code, diagnosis, prescription, notes, doctor_id, patient_id) VALUES
(601, 'Hypertension', 'Beta-blockers', 'Monitor blood pressure regularly', 5, 21),
(602, 'Migraine', 'Pain relievers', 'Patient needs rest', 6, 22),
(603, 'Lower back pain', 'Painkillers', 'Recommend physical therapy', 7, 23),
(604, 'Type 2 Diabetes', 'Insulin', 'Diet control required', 8, 24);

INSERT INTO payment (payment_code, amount, date, status, patient_id) VALUES
(701, 100.50, '2024-12-01 14:00:00', 'Paid', 21),
(702, 150.75, '2024-12-02 15:00:00', 'Paid', 22),
(703, 120.00, '2024-12-03 16:00:00', 'Unpaid', 23),
(704, 180.00, '2024-12-04 17:00:00', 'Paid', 24);


SELECT * FROM person
SELECT * FROM doctor;
SELECT * FROM patient;
SELECT time, date, patient_name, doctor_name FROM appointment_record WHERE status = 'active'

DELETE FROM appointment_record;
DELETE FROM medical_history;
DELETE FROM payment;
DELETE FROM nurse;
DELETE FROM admin;
DELETE FROM doctor;
DELETE FROM patient;
