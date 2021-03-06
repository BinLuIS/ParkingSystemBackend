package com.binluis.parkingsystem.models;

import com.binluis.parkingsystem.domain.ParkingBoy;
import com.binluis.parkingsystem.domain.ParkingLot;
import com.binluis.parkingsystem.domain.ParkingOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class ParkingOrderResponse {
    private Long id;
    private String carNumber;
    private String requestType;
    private String status;
    private ParkingLot parkingLot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public static ParkingOrderResponse create(Long id, String carNumber, String requestType, String status,ParkingLot parkingLot) {
        Objects.requireNonNull(id);

        final ParkingOrderResponse response = new ParkingOrderResponse();
        response.setId(id);
        response.setCarNumber(carNumber);
        response.setRequestType(requestType);
        response.setStatus(status);
        response.setParkingLot(parkingLot);
        return response;
    }

    public static ParkingOrderResponse create(ParkingOrder entity) {
        return create(entity.getId(),entity.getCarNumber(),entity.getRequestType(),entity.getStatus(),entity.getParkingLot());
    }

    @JsonIgnore
    public boolean isValid() {
        return id != null;
    }
}
