package lk.mega.se.data;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public interface BillDB {
    void updateBillDB(int patient_number, LocalDate invoiceDate, String service, int Quantity,String paymentMethod,Double discount,Double tax, LocalDate serviceDate, LocalTime serviceTime, double cost, double finalCost);
    ResultSet searchInvoiceNumber(int invoiceNumber);
    ArrayList<String> getServices();
    ArrayList<String> searchService(String searchingWord);
    Double getServiceCost(String service);
    ResultSet getPatientHistory(int patientId);

}
