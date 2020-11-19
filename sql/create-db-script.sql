DROP TABLE prescription IF EXISTS;
DROP TABLE doctor IF EXISTS;
DROP TABLE patient IF EXISTS;

CREATE TABLE doctor
(id BIGINT IDENTITY not null,
first_name VARCHAR(15) not null,
last_name VARCHAR(15) not null,
patronymic VARCHAR(15) not null,
specialization VARCHAR(30) not null,
CONSTRAINT doctor_pk PRIMARY KEY (id));

CREATE TABLE patient
(id BIGINT IDENTITY not null,
first_name VARCHAR(15) not null,
last_name VARCHAR(15) not null,
patronymic VARCHAR(15) not null,
phone_number VARCHAR(30) not null,
CONSTRAINT patient_pk PRIMARY KEY (id));

CREATE TABLE prescription
(id BIGINT IDENTITY not null,
doctor_id BIGINT not null,
patient_id BIGINT not null,
description VARCHAR(100) not null,
date DATE not null,
expiration_date DATE not null,
priority VARCHAR(6) not null,
CONSTRAINT prescription_pk PRIMARY KEY (id),
CONSTRAINT prescription_doctor_fk
FOREIGN KEY (doctor_id) REFERENCES doctor(id),
CONSTRAINT prescription_patient_fk
FOREIGN KEY (patient_id) REFERENCES patient(id));