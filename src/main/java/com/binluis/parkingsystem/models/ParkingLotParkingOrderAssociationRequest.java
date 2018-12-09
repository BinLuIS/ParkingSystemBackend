package com.binluis.parkingsystem.models;

public class ParkingLotParkingOrderAssociationRequest {
    //Fields
    private Long parkingOrderId;

    //Constructor

    public ParkingLotParkingOrderAssociationRequest() {
    }

    //Methods
    public Long getId() {
        return parkingOrderId;
    }

    public static ParkingLotParkingOrderAssociationRequest create(Long parkingOrderId){
        final ParkingLotParkingOrderAssociationRequest request = new ParkingLotParkingOrderAssociationRequest();
        request.parkingOrderId = parkingOrderId;
        return request;
    }

    public void setId(Long id) {
        parkingOrderId = id;
    }

    public boolean isVaild(){
        return parkingOrderId!=null;
    }


}
