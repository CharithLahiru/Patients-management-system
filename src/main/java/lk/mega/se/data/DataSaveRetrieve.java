package lk.mega.se.data;

import lk.mega.se.application.Patient;

import java.time.LocalDate;
import java.util.ArrayList;

public interface DataSaveRetrieve {
    public Patient searchPatient();
    public void updatePatientDatabase(String number, String name, String idNumber, String passportNumber, LocalDate date,String gender, Double weight, Double height, String address, ArrayList<String> contactNumbers, String email, String note);


}
