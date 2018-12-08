package com.binluis.parkingsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

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
    //https://www.jianshu.com/p/e8caafce5445
    @OneToMany(cascade = {CascadeType.MERGE}, mappedBy = "parkingLot")
    private List<ParkingOrder> parkingOrders;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parking_boy_id")
    private ParkingBoy parkingBoy;


    public ParkingLot() {
    }

    public ParkingLot(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }


    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<ParkingOrder> getParkingOrders() {
        return parkingOrders;
    }

    public void setParkingOrders(List<ParkingOrder> parkingOrders) {
        this.parkingOrders = parkingOrders;

    }

    public ParkingBoy getParkingBoy() {
        return parkingBoy;
    }

    public void setParkingBoy(ParkingBoy parkingBoy) {
        this.parkingBoy = parkingBoy;
    }
}