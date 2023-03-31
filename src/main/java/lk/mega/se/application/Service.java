package lk.mega.se.application;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Service implements Serializable {
    private String service;
    private int qty;
    private LocalDate serviceDate;
    private LocalTime serviceTime;
    private Double cost;


    public Service() {
    }

    public Service(String service, int qty, LocalDate serviceDate, LocalTime serviceTime, Double cost) {
        this.service = service;
        this.qty = qty;
        this.serviceDate = serviceDate;
        this.serviceTime = serviceTime;
        this.cost = cost;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public LocalDate getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
    }

    public LocalTime getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(LocalTime serviceTime) {
        this.serviceTime = serviceTime;
    }
}
