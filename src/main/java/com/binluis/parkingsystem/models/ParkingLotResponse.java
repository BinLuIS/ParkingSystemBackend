package com.binluis.parkingsystem.models;

import com.binluis.parkingsystem.domain.ParkingLot;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class ParkingLotResponse {
    private Long id;
    private String name;
    private int capacity;

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

    public static ParkingLotResponse create(Long id,String name, int capacity) {
        Objects.requireNonNull(id);

        final ParkingLotResponse response = new ParkingLotResponse();
        response.setId(id);
        response.setName(name);
        response.setCapacity(capacity);
        return response;
    }

    public static ParkingLotResponse create(ParkingLot entity) {
        return create(entity.getId(),entity.getName(),entity.getCapacity());
    }

    @JsonIgnore
    public boolean isValid() {
        return id != null;
    }
}
