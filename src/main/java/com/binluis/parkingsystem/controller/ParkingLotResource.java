package com.binluis.parkingsystem.controller;


import com.binluis.parkingsystem.domain.*;
import com.binluis.parkingsystem.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@CrossOrigin(origins = {"http://localhost:3000","https://parkingwebappmobile.herokuapp.com","https://binluis-parkingwebapp.herokuapp.com"})
@RequestMapping("/parkinglots")
public class ParkingLotResource {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    ParkingLotRepository parkingLotRepository;

    @Autowired
    ParkingOrderRepository parkingOrderRepository;

    @GetMapping
    public ResponseEntity<ParkingLotResponse[]> getAllParkingLots() {
        final ParkingLotResponse[] parkingLotResponses= parkingLotRepository.findAll().stream()
                .map(ParkingLotResponse::create)
                .toArray(ParkingLotResponse[]::new);
        return ResponseEntity.ok(parkingLotResponses);
    }


    @PostMapping
    public ResponseEntity createParkingLot(@RequestBody ParkingLot parkingLot) {
        final ParkingLotResponse parkingLotResponse = ParkingLotResponse.create(parkingLotRepository.save(parkingLot));
        return ResponseEntity.created(URI.create("/parkinglots")).body(parkingLotResponse);
    }


    @GetMapping(path = "/{id}/orders")
    public ResponseEntity<ParkingOrderResponse[]> getAllAssociateParkingOrders(@PathVariable Long id){
        ParkingLot parkingLot = parkingLotRepository.findOneById(id);
        return ResponseEntity.ok((ParkingOrderResponse[])parkingLot.getParkingOrders().stream()
                .map(ParkingOrderResponse::create)
                .toArray(ParkingOrderResponse[]::new));
    }

    @PostMapping(path = "/{id}/orders")
    public ResponseEntity associateParkingLotWithParkingOrder(
            @PathVariable Long id,
            @RequestBody ParkingLotParkingOrderAssociationRequest request
    ){
        if(!request.isVaild()){
            return ResponseEntity.badRequest().build();
        }

        final ParkingLot parkingLot = parkingLotRepository.findOneById(id);
        if(parkingLot == null){
            return ResponseEntity.badRequest().build();
        }

        final ParkingOrder parkingOrder = parkingOrderRepository.findOneById(request.getParkingOrderId());
        if(parkingOrder.getParkingLot()!=null){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        parkingOrder.setParkingLot(parkingLot);
        parkingLot.getParkingOrders().add(parkingOrder);
        parkingOrder.setStatus("parked");
        parkingOrderRepository.saveAndFlush(parkingOrder);
        int availableCapacity = parkingLot.getCapacity()-parkingLot.getParkingOrders().size();
        ParkingLotResponse parkingLotResponse = ParkingLotResponse.create(parkingLot.getId(), parkingLot.getName(), parkingLot.getCapacity(), availableCapacity);
        return ResponseEntity.created(URI.create("parkinglots/"+parkingLot.getId()+"/orders")).body(parkingLotResponse);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ParkingLot> getParkingLotById (@PathVariable Long id){
        Optional<ParkingLot> parkingLot = parkingLotRepository.findById(id);
        if(!parkingLot.isPresent()){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(parkingLot.get());

    }

    @PutMapping(path = "/{id}/orders/{carNumber}")
    public ResponseEntity<ParkingOrder> fetchCarFromParkingLot(@PathVariable Long id, @PathVariable String carNumber){
        Optional<ParkingLot> parkingLot = parkingLotRepository.findById(id);
        int removedIndex = -1;
        Long orderId = null;
        if(!parkingLot.isPresent()){
            return ResponseEntity.badRequest().build();
        }
        int i=0;
        for (ParkingOrder parkingOrder: parkingLot.get().getParkingOrders()) {
            if(parkingOrder.getCarNumber().equals(carNumber)){
                removedIndex = i;
                orderId = parkingOrder.getId();
            }
            i++;
        }
        if(removedIndex==-1){
            return ResponseEntity.badRequest().build();
        }
        parkingLot.get().getParkingOrders().remove(removedIndex);
        parkingLotRepository.save(parkingLot.get());

        Optional<ParkingOrder> parkingOrder = parkingOrderRepository.findById(orderId);
        if(!parkingOrder.isPresent()){
            return ResponseEntity.badRequest().build();
        }
        parkingOrder.get().setParkingLot(null);
        parkingOrderRepository.saveAndFlush(parkingOrder.get());
        return ResponseEntity.ok().body(parkingOrder.get());
    }







}