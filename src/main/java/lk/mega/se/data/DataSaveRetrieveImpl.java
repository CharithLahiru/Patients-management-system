package lk.mega.se.data;

import lk.mega.se.application.Patient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class DataSaveRetrieveImpl implements DataSaveRetrieve {

    public DataSaveRetrieveImpl() {
        generateTables();
    }

    private void generateTables() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet rst = statement.executeQuery("SHOW TABLES");
            if (!rst.next()) {
                InputStream is = getClass().getResourceAsStream("/schema.sql");
                InputStreamReader inputStreamReader = new InputStreamReader(is);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                statement.execute(stringBuilder.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Patient searchPatient() {
        System.out.println("Return a patient");
        return null;
    }

    @Override
    public void updatePatientDatabase(String number, String name, String idNumber, String passportNumber, LocalDate date, String gender, Double weight, Double height, String address, ArrayList<String> contactNumbers, String email, String note) {
        System.out.println("data come to the database");
    }


}
