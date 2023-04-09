package lk.mega.se.data;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class BillDBImpl implements BillDB{

    private Connection connection = DBConnection.getInstance().getConnection();


    @Override
    public void updateBillDB(int patient_number, LocalDate invoiceDate, String service, int Quantity,String paymentMethod,Double discount,Double tax, LocalDate serviceDate, LocalTime serviceTime, double cost, double finalCost) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Invoice (invoice_date, patient_number, service_date, service_time, service, qty, payment_method,cost, discount, tax, final_cost) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setDate(1, java.sql.Date.valueOf(invoiceDate));
            preparedStatement.setInt(2,patient_number);
            preparedStatement.setDate(3, Date.valueOf(serviceDate));
            preparedStatement.setTime(4, Time.valueOf(serviceTime));
            preparedStatement.setString(5,service);
            preparedStatement.setInt(6,Quantity);
            preparedStatement.setString(7,paymentMethod);
            preparedStatement.setDouble(8,cost);
            preparedStatement.setDouble(9,discount);
            preparedStatement.setDouble(10,tax);
            preparedStatement.setDouble(11,finalCost);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResultSet searchInvoiceNumber(int invoiceNumber) {
        return null;
    }


    @Override
    public ArrayList<String> getServices() {
        try {
            Statement stm = connection.createStatement();
            ResultSet rstService = stm.executeQuery("SELECT service FROM Service");
            ArrayList<String> services = new ArrayList<>();
            while (rstService.next()){
                services.add(rstService.getString("service"));
            }
            return services;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<String> searchService(String searchingWord) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(String.format("SELECT service FROM Service WHERE service LIKE %s", "'"+searchingWord+"%'"));
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<String> services = new ArrayList<>();
            while (resultSet.next()){
                services.add(resultSet.getString("service"));
            }
            return services;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Double getServiceCost(String service) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT price FROM Service WHERE service = ?");
            preparedStatement.setString(1,service);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getDouble("price");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResultSet getPatientHistory(int patientId) {
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM Invoice WHERE patient_number="+patientId;
            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
