DROP TABLE IF EXISTS Patient;
DROP TABLE IF EXISTS Contact;

CREATE TABLE Patient(
    image MEDIUMBLOB,
    patient_number INTEGER  PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    id_number VARCHAR(20) NOT NULL,
    passport_number VARCHAR(20) ,
    birthday DATE ,
    age INTEGER,
    gender VARCHAR(10),
    height DOUBLE PRECISION ,
    weight DOUBLE PRECISION ,
    address VARCHAR(100),
    email VARCHAR(50),
    note MEDIUMTEXT
);

CREATE TABLE Contacts(
    patient_number VARCHAR(10) NOT NULL ,
    contact_number varchar(20),
    CONSTRAINT PRIMARY KEY (patient_number,contact_number )
);