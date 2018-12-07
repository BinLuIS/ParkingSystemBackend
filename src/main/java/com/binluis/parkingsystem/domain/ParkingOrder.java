package com.binluis.parkingsystem.domain;

import javax.persistence.*;

@Entity
@Table(name = "parking_order")
public class ParkingOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "carNumber")
    private String carNumber;
    @Column(name = "requestType")
    private String requestType;
    @Column(name = "status")
    private String status;
    private

    public ParkingOrder() {
    }

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
}