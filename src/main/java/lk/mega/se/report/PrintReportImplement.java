package lk.mega.se.report;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.util.HashMap;

public class PrintReportImplement implements PrintReport{

    @Override
    public void printJasperReport() {
        try {
            JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("/reports/Invoice2.jrxml"));
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            HashMap<String, Object> reportParams = new HashMap<>();
            JREmptyDataSource dataSource = new JREmptyDataSource(1);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportParams, dataSource);
            JasperViewer.viewReport(jasperPrint,false);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
