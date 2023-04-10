package lk.mega.se.report;

import lk.mega.se.application.Service;

public interface PrintReport {
    void printJasperReport(String printDate,String invoiceNo, String name, String contact, String address, String paymentMethod, String discount, String tax, String total,
                           String service, String qty, String serviceDate, String serviceTime, String cost);
}
