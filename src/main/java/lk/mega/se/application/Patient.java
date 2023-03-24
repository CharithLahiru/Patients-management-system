package lk.mega.se.application;

import javafx.scene.image.ImageView;

import java.time.LocalDate;

public class Patient extends Person {
    private String patientNumber;
    private double weight;
    private double height;
    private String note;

    public Patient() {
    }

    public Patient(ImageView imageView, String name, String idNumber, String passportNumber, LocalDate birthday, int age, Gender gender, String contactNumber, String address, String email, String patientNumber, double weight, double height, String note) {
        super(imageView, name, idNumber, passportNumber, birthday, age, gender, contactNumber, address, email);
        this.patientNumber = patientNumber;
        this.weight = weight;
        this.height = height;
        this.note = note;
    }

    public Patient(String patientNumber, double weight, double height, String note) {
        this.patientNumber = patientNumber;
        this.weight = weight;
        this.height = height;
        this.note = note;
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
