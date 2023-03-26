package lk.mega.se.data;

import javafx.scene.image.Image;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

public interface DataSaveRetrieve {
    public void updatePatientDatabase(Image image,String name, String idNumber, String passportNumber, LocalDate date, String gender, Double weight, Double height, String address, ArrayList<String> contactNumbers, String email, String note);
    public ResultSet searchPatientsIdNumber(int number);
    public ResultSet searchPatientsName(String searchingWord);
    public ResultSet searchPatientsIdNumber(String searchingWord);
    public ResultSet searchPatientsPassportNumber(String searchingWord);
    public void deletePatient(int patientNumber);
    public int lastPatientID();
    public Image retrievePatientImage(int number);
}

