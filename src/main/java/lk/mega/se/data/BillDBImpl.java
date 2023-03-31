package lk.mega.se.data;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class BillDBImpl implements BillDB{

    private Connection connection = DBConnection.getInstance().getConnection();
    @Override
    public void updateBillDB(int patient_number, Date date, LocalTime localTime, String service, int Quantity, double final_cost) {

    }

    @Override
    public ResultSet searchInvoiceNumber(int invoiceNumber) {
        return null;
    }

    @Override
    public ResultSet searchPatientNumber(int patientNumber) {
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
}
