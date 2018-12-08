package com.binluis.parkingsystem.models;

public class ParkingBoyParkingOrderAssociationRequest {
    private Long parkingOrderId;

    public ParkingBoyParkingOrderAssociationRequest() {
    }

    public ParkingBoyParkingOrderAssociationRequest(Long parkingOrderId) {
        this.parkingOrderId = parkingOrderId;
    }

    public Long getParkingOrderId() {
        return parkingOrderId;
    }
}
