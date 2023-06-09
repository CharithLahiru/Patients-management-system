
CREATE TABLE IF NOT EXISTS Patient(
    patient_number INTEGER  PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    id_number VARCHAR(20) NOT NULL,
    passport_number VARCHAR(20) ,
    birthday DATE ,
    gender VARCHAR(10),
    address VARCHAR(100),
    email VARCHAR(50),
    note MEDIUMTEXT
);

CREATE TABLE IF NOT EXISTS Contacts(
    patient_number VARCHAR(10) NOT NULL ,
    contact_number varchar(20),
    CONSTRAINT PRIMARY KEY (patient_number,contact_number )
);

CREATE TABLE  IF NOT EXISTS Service(
    service VARCHAR(50) PRIMARY KEY,
    price DOUBLE NOT NULL
);

CREATE TABLE  IF NOT EXISTS Invoice(
    invoice_number INTEGER PRIMARY KEY AUTO_INCREMENT,
    invoice_date DATE NOT NULL,
    patient_number INTEGER NOT NULL ,
    service_date DATE ,
    service_time TIME,
    service VARCHAR(50) NOT NULL ,
    qty INTEGER NOT NULL,
    payment_method VARCHAR(5) NOT NULL ,
    cost DOUBLE NOT NULL ,
    discount DOUBLE NOT NULL,
    tax DOUBLE NOT NULL,
    final_cost INTEGER NOT NULL,
    CONSTRAINT fk_service FOREIGN KEY (service) REFERENCES Service(service),
    CONSTRAINT fk_patient_number FOREIGN KEY (patient_number) REFERENCES Patient(patient_number)
)