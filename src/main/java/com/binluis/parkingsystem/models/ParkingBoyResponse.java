package com.binluis.parkingsystem.models;

import com.binluis.parkingsystem.domain.ParkingBoy;
import com.binluis.parkingsystem.domain.ParkingLot;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParkingBoyResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String status;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public static ParkingBoyResponse create(Long id,String name,String email,String phoneNumber,String status) {
        Objects.requireNonNull(id);

        final ParkingBoyResponse response = new ParkingBoyResponse();
        response.setId(id);
        response.setName(name);
        response.setEmail(email);
        response.setPhoneNumber(phoneNumber);
        response.setStatus(status);
        return response;
    }

    public static ParkingBoyResponse create(ParkingBoy entity) {
        return create(entity.getId(),entity.getName(),entity.getEmail(),entity.getPhoneNumber(),entity.getStatus());
    }

    @JsonIgnore
    public boolean isValid() {
        return id!= null;
    }


}
