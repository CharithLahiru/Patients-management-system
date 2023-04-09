package lk.mega.se.application;

import java.io.Serializable;
import java.util.Date;

public class PatientHistory implements Serializable {
    private String invoiceNo;
    private String invoiceDate;
    private String service;
    private String qty;
    private String serviceDate;
    private String serviceTime;
    private String cost;
    private String paymentMethod;
    private String discount;
    private String tax;
    private String totalCost;

    public PatientHistory() {
    }

    public PatientHistory(String invoiceNo, String invoiceDate, String service, String qty, String serviceDate, String serviceTime, String cost, String paymentMethod, String discount, String tax, String totalCost) {
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.service = service;
        this.qty = qty;
        this.serviceDate = serviceDate;
        this.serviceTime = serviceTime;
        this.cost = cost;
        this.paymentMethod = paymentMethod;
        this.discount = discount;
        this.tax = tax;
        this.totalCost = totalCost;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
