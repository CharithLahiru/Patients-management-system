package lk.mega.se.data;

import javafx.scene.image.Image;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface DataSaveRetrieve {
    void generateTables();
    public void updatePatientDatabase(String name, String idNumber, String passportNumber, LocalDate date, String gender, String address, ArrayList<String> contactNumbers, String email, String note);
    public ResultSet searchPatientsIdNumber(int number);
    public ResultSet searchPatientsName(String searchingWord);
    public ResultSet searchPatientsIdNumber(String searchingWord);
    public ResultSet searchPatientsPassportNumber(String searchingWord);
    public void deletePatient(int patientNumber);
    List<String> getContactList(int patientNumber);
}

