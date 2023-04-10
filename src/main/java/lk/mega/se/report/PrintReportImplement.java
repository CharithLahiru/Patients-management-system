package lk.mega.se.report;

import lk.mega.se.application.Service;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.util.HashMap;

public class PrintReportImplement implements PrintReport{

    @Override
    public void printJasperReport(String printDate,String invoiceNo, String name, String contact, String address, String paymentMethod, String discount, String tax, String total ,
                                  String service, String qty, String serviceDate, String serviceTime, String cost) {
        try {
            JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("/reports/Invoice2.jrxml"));
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            HashMap<String, Object> reportParams = new HashMap<>();
            reportParams.put("printDate",printDate);
            reportParams.put("invoiceNo",invoiceNo);
            reportParams.put("name",name);
            reportParams.put("contact",contact);
            reportParams.put("address",address);
            reportParams.put("paymentMethod",paymentMethod);
            reportParams.put("discount",discount);
            reportParams.put("tax",tax);
            reportParams.put("total",total);
            reportParams.put("service",service);
            reportParams.put("serviceDate",serviceDate);
            reportParams.put("serviceTime",serviceTime);
            reportParams.put("qty",qty);
            reportParams.put("cost",cost);


            JREmptyDataSource dataSource = new JREmptyDataSource(1);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportParams, dataSource);
            JasperViewer.viewReport(jasperPrint,false);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
