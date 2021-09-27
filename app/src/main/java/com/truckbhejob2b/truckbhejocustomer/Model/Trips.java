package com.truckbhejob2b.truckbhejocustomer.Model;

public class Trips {
    private String orderId;
    private String fromLocation;
    private String toLocation;
    private String orderStatus;
    private String podStatus;
    private String driverNumber;
    private String vehicleType;
    private String orderDate;
    private String vehicleNumber;
    private String trackStatus;
    private String productType;

    public Trips() {
        this.orderId = orderId;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.orderStatus = orderStatus;
        this.podStatus = podStatus;
        this.driverNumber = driverNumber;
        this.vehicleType = vehicleType;
        this.orderDate = orderDate;
        this.vehicleNumber = vehicleNumber;
        this.trackStatus = trackStatus;
        this.productType = productType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPodStatus() {
        return podStatus;
    }

    public void setPodStatus(String podStatus) {
        this.podStatus = podStatus;
    }

    public String getDriverNumber() {
        return driverNumber;
    }

    public void setDriverNumber(String driverNumber) {
        this.driverNumber = driverNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getTrackStatus() {
        return trackStatus;
    }

    public void setTrackStatus(String trackStatus) {
        this.trackStatus = trackStatus;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
}
