package com.binluis.parkingsystem.controller;

import com.binluis.parkingsystem.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    ParkingBoyRepository parkingBoyRepository;

    @Autowired
    ParkingOrderRepository parkingOrderRepository;

    @Autowired
    ParkingLotRepository parkingLotRepository;

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

    @GetMapping(value = "/temp",produces = {"application/json"})
    public ParkingLot listParkingLot_Joe_Test() {
        ParkingLot parkingLot=new ParkingLot("LotA",10);
        parkingLotRepository.save(parkingLot);
        ParkingOrder parkingOrder = new ParkingOrder("car2","park","accept");
        parkingOrder.setParkingLot(parkingLot);
        parkingOrderRepository.save(parkingOrder);
        parkingLotRepository.save(parkingLot);
        return parkingLotRepository.findAll().get(0);
    }


//    @GetMapping(produces = {"application/json"})
//    public String list() {
//        return "{test:success}";
//    }

}