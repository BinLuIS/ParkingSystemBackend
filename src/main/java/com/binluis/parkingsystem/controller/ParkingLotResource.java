package com.binluis.parkingsystem.controller;


import com.binluis.parkingsystem.domain.*;
import com.binluis.parkingsystem.models.ParkingBoyParkingOrderAssociationRequest;
import com.binluis.parkingsystem.models.ParkingBoyResponse;
import com.binluis.parkingsystem.models.ParkingLotParkingOrderAssociationRequest;
import com.binluis.parkingsystem.models.ParkingLotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/parkinglots")
public class ParkingLotResource {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    ParkingLotRepository parkingLotRepository;

    @Autowired
    ParkingOrderRepository parkingOrderRepository;

    @GetMapping
    public ResponseEntity<ParkingLotResponse[]> getAll() {
        final ParkingLotResponse[] parkingLotResponses= parkingLotRepository.findAll().stream()
                .map(ParkingLotResponse::create)
                .toArray(ParkingLotResponse[]::new);
        return ResponseEntity.ok(parkingLotResponses);
    }

    @PostMapping(path = "/{parkingLotName}/orders")
    public ResponseEntity associateParkingLotWithParkingOrder(
            @RequestBody String parkingLotName,
            @RequestBody ParkingLotParkingOrderAssociationRequest request
            ){
        if(!request.isVaild()){
            return ResponseEntity.badRequest().body("Invaild Car Number");
        }

        final ParkingLot parkingLot = parkingLotRepository.findOneByParkingLotName(parkingLotName);
        if(parkingLot == null){
            return ResponseEntity.badRequest().body("Invaild Parking Lot");
        }

        final ParkingOrder parkingOrder = parkingOrderRepository.findOneByCarName(request.getCarNumber());
        if(parkingOrder.getParkingLot()!=null){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        parkingOrder.setParkingLot(parkingLot);
        parkingOrderRepository.saveAndFlush(parkingOrder);
        return ResponseEntity.created(URI.create("parkinglots/{parkingLotName}/orders/"+request.getCarNumber())).body("Added Parking Order to Parking Lots");



    }


}
