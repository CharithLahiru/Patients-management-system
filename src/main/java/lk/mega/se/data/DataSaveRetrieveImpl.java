package lk.mega.se.data;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DataSaveRetrieveImpl implements DataSaveRetrieve {

    Connection connection = DBConnection.getInstance().getConnection();


    public void generateTables() {
        try {
            Statement statement = connection.createStatement();
            ResultSet rst = statement.executeQuery("SHOW TABLES");
            HashSet<String> tableSet = new HashSet<>();
            while (rst.next()){
                tableSet.add(rst.getString(1));
            }
            if (!tableSet.containsAll(Set.of("Patient","Contacts","Service","Invoice"))){
                System.out.println("Schema is about to auto generate");
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePatientDatabase(String name, String idNumber, String passportNumber, LocalDate birthday, String gender, String address, ArrayList<String> contactNumbers, String email, String note) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement stm = connection.prepareStatement("INSERT INTO Patient (image,name, id_number,passport_number,birthday,age,gender,height,weight,address,email,note) VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?)");
            stm.setString(1,name);
            stm.setString(2,idNumber);
            stm.setString(3,passportNumber);
            stm.setDate(4, Date.valueOf(birthday));
            stm.setString(5,gender.toString());
            stm.setString(6,address);
            stm.setString(7,email);
            stm.setString(8,note);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResultSet searchPatientsName(String searchingWord) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(String.format("SELECT * FROM Patient WHERE name LIKE %s", "'"+searchingWord+"%'"));
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResultSet searchPatientsIdNumber(String searchingWord) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(String.format("SELECT * FROM Patient WHERE id_number LIKE %s", "'"+searchingWord+"%'"));
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResultSet searchPatientsPassportNumber(String searchingWord) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(String.format("SELECT * FROM Patient WHERE passport_number LIKE %s", "'"+searchingWord+"%'"));
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deletePatient(int patientNumber) {
        String sql = String.format("DELETE FROM Patient WHERE patient_number='%d'",patientNumber);
        try {
            connection.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResultSet searchPatientsIdNumber(int number) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(String.format("SELECT * FROM Patient WHERE patient_number = %s", "'"+number+"%'"));
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int lastPatientID() {
        try {
            String sql = "SELECT * FROM Patient ORDER BY patient_Number DESC LIMIT 1";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            return resultSet.getInt("name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
