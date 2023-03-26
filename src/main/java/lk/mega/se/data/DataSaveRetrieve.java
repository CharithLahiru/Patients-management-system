package lk.mega.se.data;

import javafx.scene.image.Image;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

public interface DataSaveRetrieve {
    public void updatePatientDatabase(Image image,String name, String idNumber, String passportNumber, LocalDate date, String gender, Double weight, Double height, String address, ArrayList<String> contactNumbers, String email, String note);
    public ResultSet searchPatientsName(String searchingWord);
    public ResultSet searchPatientsId(int number);
    public int lastPatientID();
}

