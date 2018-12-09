package com.binluis.parkingsystem.models;

public class ParkingLotParkingOrderAssociationRequest {
    //Fields
    private Long parkingOrderId;

    //Constructor

    public ParkingLotParkingOrderAssociationRequest() {
    }

    //Methods


    public Long getParkingOrderId() {
        return parkingOrderId;
    }

    public void setParkingOrderId(Long parkingOrderId) {
        this.parkingOrderId = parkingOrderId;
    }

    public static ParkingLotParkingOrderAssociationRequest create(Long parkingOrderId){
        final ParkingLotParkingOrderAssociationRequest request = new ParkingLotParkingOrderAssociationRequest();
        request.parkingOrderId = parkingOrderId;
        return request;
    }


    public boolean isVaild(){
        return parkingOrderId!=null;
    }


}
