package com.binluis.parkingsystem.models;

public class ParkingLotParkingOrderAssociationRequest {
    //Fields
    private String carNumber;

    //Methods
    public String getCarNumber() {
        return carNumber;
    }

    public static ParkingLotParkingOrderAssociationRequest create(String carNumber){
        final ParkingLotParkingOrderAssociationRequest request = new ParkingLotParkingOrderAssociationRequest();
        request.carNumber = carNumber;
        return request;
    }

    public boolean isVaild(){
        return carNumber!=null;
    }


}
