package com.binluis.parkingsystem.models;

public class ParkingBoyParkingLotAssociationRequest {
    private Long parkingLotId;

    public ParkingBoyParkingLotAssociationRequest() {
    }

    public ParkingBoyParkingLotAssociationRequest(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public Long getParkingLotId() {
        return parkingLotId;
    }
}
