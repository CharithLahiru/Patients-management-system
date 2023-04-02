package lk.mega.se.application;

import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.time.LocalDate;

public class Person implements Serializable {
    private String name;
    private String idNumber;
    private String passportNumber;
    private LocalDate birthday;
    private int age;
    private Gender gender;
    private String contactNumber;
    private String address;
    private String email;

    public Person(String name, String idNumber, String passportNumber, LocalDate birthday, int age, Gender gender, String contactNumber, String address, String email) {
        this.name = name;
        this.idNumber = idNumber;
        this.passportNumber = passportNumber;
        this.birthday = birthday;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.address = address;
        this.email = email;
    }
    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
