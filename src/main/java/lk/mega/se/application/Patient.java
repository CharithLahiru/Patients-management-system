package lk.mega.se.application;

import javafx.scene.image.ImageView;

import java.time.LocalDate;

public class Patient extends Person {
    private String patientNumber;
    private String note;

    public Patient() {
    }

    public Patient(ImageView imageView, String name, String idNumber, String passportNumber, LocalDate birthday, int age, Gender gender, String contactNumber, String address, String email, String patientNumber, double weight, double height, String note) {
        super(name, idNumber, passportNumber, birthday, age, gender, contactNumber, address, email);
        this.patientNumber = patientNumber;
        this.note = note;
    }

    public Patient(String patientNumber, double weight, double height, String note) {
        this.patientNumber = patientNumber;
        this.note = note;
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
