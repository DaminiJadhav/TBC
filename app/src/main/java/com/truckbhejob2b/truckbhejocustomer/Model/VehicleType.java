package com.truckbhejob2b.truckbhejocustomer.Model;

public class VehicleType {
    private String vehicleTypeId;
    private String vehicleTypeName;
    private String vehicleCapacity;

    public String getVehicleCapacity() {
        return vehicleCapacity;
    }
    public void setVehicleCapacity(String vehicleCapacity) {
        this.vehicleCapacity = vehicleCapacity;
    }
    public String getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(String vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }
}
