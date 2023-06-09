package lk.mega.se.application;

import javafx.scene.image.ImageView;

import java.time.LocalDate;
import java.util.List;

public class Patient extends Person {
    private String patientNumber;
    private String note;

    public Patient() {
    }

    public Patient(String name, String idNumber, String passportNumber, LocalDate birthday, int age, Gender gender, List<String> contactNumber, String address, String email, String patientNumber, String note) {
        super(name, idNumber, passportNumber, birthday, age, gender, contactNumber, address, email);
        this.patientNumber = patientNumber;
        this.note = note;
    }

    public Patient(String patientNumber, String note) {
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
