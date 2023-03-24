DROP TABLE IF EXISTS Patient;
DROP TABLE IF EXISTS Contact;

CREATE TABLE Patient(
    patient_number VARCHAR(10) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    id_number VARCHAR(20) NOT NULL,
    passport_number VARCHAR(20) ,
    height DOUBLE PRECISION ,
    weight DOUBLE PRECISION ,
    birthday DATE,
    age INTEGER,
    address VARCHAR(100),
    email VARCHAR(50),
    note VARCHAR(1000)
);

CREATE TABLE Contacts(
    patient_number VARCHAR(10) NOT NULL ,
    contact_number varchar(20),
    CONSTRAINT PRIMARY KEY (patient_number,contact_number )
);