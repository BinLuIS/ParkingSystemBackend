package com.binluis.parkingsystem.models;

import com.binluis.parkingsystem.domain.ParkingBoy;
import com.binluis.parkingsystem.domain.ParkingLot;

public class CreateParkingOrderRequest {
    //Fields
    private String carNumber;
    private String requestType;
    private String status;
    private ParkingBoy parkingBoy;
    private ParkingLot parkingLot;

    //Methods
    public String getCarNumber() {
        return carNumber;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getStatus() {
        return status;
    }

    public ParkingBoy getParkingBoy() {
        return parkingBoy;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public static CreateParkingOrderRequest create(String carNumber){
        final CreateParkingOrderRequest request = new CreateParkingOrderRequest();
        request.carNumber = carNumber;
        request.requestType = "Parking";
        request.status = "Pending";
        request.parkingBoy = null;
        request.parkingLot = null;
        if(!request.isVaild()){
            throw new IllegalArgumentException("employeeId:" + carNumber);
        }
        return request;
    }

    public boolean isVaild(){
        return carNumber != null && !carNumber.isEmpty();
    }
}
