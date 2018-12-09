package com.binluis.parkingsystem.models;

public class ParkingLotParkingOrderAssociationRequest {
    //Fields
    private Long Id;

    //Constructor

    public ParkingLotParkingOrderAssociationRequest() {
    }

    //Methods
    public Long getId() {
        return Id;
    }

    public static ParkingLotParkingOrderAssociationRequest create(Long Id){
        final ParkingLotParkingOrderAssociationRequest request = new ParkingLotParkingOrderAssociationRequest();
        request.Id = Id;
        return request;
    }

    public void setId(Long id) {
        Id = id;
    }

    public boolean isVaild(){
        return Id!=null;
    }


}
