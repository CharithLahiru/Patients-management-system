package lk.mega.se.data;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DataSaveRetrieveImpl implements DataSaveRetrieve {

    Connection connection = DBConnection.getInstance().getConnection();

    public DataSaveRetrieveImpl() {
        generateTables();
    }

    private void generateTables() {
        try {
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
    public void updatePatientDatabase(Image image, String name, String idNumber, String passportNumber, LocalDate birthday, String gender, Double weight, Double height, String address, ArrayList<String> contactNumbers, String email, String note) {
        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage,"png",byteArrayOutputStream);
            Blob blob = new SerialBlob(byteArrayOutputStream.toByteArray());
            int age = 0;
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement stm = connection.prepareStatement("INSERT INTO Patient (image,name, id_number,passport_number,birthday,age,gender,height,weight,address,email,note) VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?)");
            stm.setBlob(1,blob);
            stm.setString(2,name);
            stm.setString(3,idNumber);
            stm.setString(4,passportNumber);
            stm.setDate(5, Date.valueOf(birthday));
            stm.setInt(6,age);
            stm.setString(7,gender.toString());
            stm.setDouble(8,(height==null)?0:height);
            stm.setDouble(9,(weight==null)?0:weight);
            stm.setString(10,address);
            stm.setString(11,email);
            stm.setString(12,note);
            stm.executeUpdate();
        } catch (SQLException | IOException e) {
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
            return resultSet.getInt(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Image retrievePatientImage(int number) {
        try {
            String sql = String.format("SELECT image FROM Patient WHERE patient_number='%d' ",number);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            Blob blob = resultSet.getBlob("image");
            if (blob==null){
                return new Image("/images/hospital.png");
            }else {
                Image image = new Image(blob.getBinaryStream(), 150, 150, true, true);
                return image;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
