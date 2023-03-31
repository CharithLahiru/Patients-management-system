package lk.mega.se.data;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public interface BillDB {
    void updateBillDB(int patient_number, Date date, LocalTime localTime, String service, int Quantity, double final_cost);
    ResultSet searchInvoiceNumber(int invoiceNumber);
    ResultSet searchPatientNumber(int patientNumber);
    ArrayList<String> getServices();
    ArrayList<String> searchService(String searchingWord);
    Double getServiceCost(String service);

}
