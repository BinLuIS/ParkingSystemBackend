package com.binluis.parkingsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "parking_lot")
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "capacity")
    private int capacity;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parking_boy_id")
    private ParkingBoy parkingBoy;


    public ParkingLot() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public ParkingBoy getParkingBoy() {
        return parkingBoy;
    }
}