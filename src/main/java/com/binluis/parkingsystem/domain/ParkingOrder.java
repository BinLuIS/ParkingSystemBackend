package com.binluis.parkingsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "parking_order")
public class ParkingOrder {
    //fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "car_number")
    private String carNumber;
    @Column(name = "request_type")
    private String requestType; // Parking   Fetching
    @Column(name = "status")
    private String status; // Pending   Accepted    Parked
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parking_boy_id")
    private ParkingBoy parkingBoy;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parking_lot_id")
    private ParkingLot parkingLot;

    //Constructors
    public ParkingOrder() {
    }

    public ParkingOrder(String carNumber, String requestType, String status) {
        this.carNumber = carNumber;
        this.requestType = requestType;
        this.parkingBoy = null;
        this.status = status;
    }

    //Methods
    public Long getId() {
        return id;
    }

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

    public void setParkingBoy(ParkingBoy parkingBoy) {
        this.parkingBoy = parkingBoy;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ParkingLot getParkingLot() { return parkingLot; }

    public void setParkingLot(ParkingLot parkingLot) { this.parkingLot = parkingLot; }
}