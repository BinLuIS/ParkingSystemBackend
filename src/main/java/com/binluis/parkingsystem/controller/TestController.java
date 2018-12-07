package com.binluis.parkingsystem.controller;

import com.binluis.parkingsystem.domain.ParkingBoy;
import com.binluis.parkingsystem.domain.ParkingBoyRepository;
import com.binluis.parkingsystem.domain.ParkingOrder;
import com.binluis.parkingsystem.domain.ParkingOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    ParkingBoyRepository parkingBoyRepository;

    @Autowired
    ParkingOrderRepository parkingOrderRepository;

    @GetMapping(produces = {"application/json"})
    public ParkingBoy list() {
        ParkingBoy parkingBoy=new ParkingBoy("name","email","12345678901","available");
        parkingBoyRepository.save(parkingBoy);
        ParkingOrder parkingOrder = new ParkingOrder("car1","park","accept");
        parkingOrder.setParkingBoy(parkingBoy);
        parkingOrderRepository.save(parkingOrder);
        parkingBoyRepository.save(parkingBoy);
        return parkingBoyRepository.findAll().get(0);
    }


//    @GetMapping(produces = {"application/json"})
//    public String list() {
//        return "{test:success}";
//    }

}