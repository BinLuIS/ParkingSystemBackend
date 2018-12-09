package com.binluis.parkingsystem.controller;

import com.binluis.parkingsystem.domain.*;
import com.binluis.parkingsystem.models.ParkingBoyParkingLotAssociationRequest;
import com.binluis.parkingsystem.models.ParkingBoyParkingOrderAssociationRequest;
import com.binluis.parkingsystem.models.ParkingBoyResponse;
import com.binluis.parkingsystem.models.ParkingOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@CrossOrigin(origins = {"http://localhost:3000","https://parkingwebappmobile.herokuapp.com","https://binluis-parkingwebapp.herokuapp.com"})
@RequestMapping("/parkingclerks")

public class ParkingBoyResource {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    ParkingBoyRepository parkingBoyRepository;

    @Autowired
    ParkingOrderRepository parkingOrderRepository;

    @Autowired
    ParkingLotRepository parkingLotRepository;

    @GetMapping
    public ResponseEntity<ParkingBoyResponse[]> getAllParkingBoys() {
        final ParkingBoyResponse[] parkingBoys = parkingBoyRepository.findAll().stream()
                .map(ParkingBoyResponse::create)
                .toArray(ParkingBoyResponse[]::new);
        return ResponseEntity.ok(parkingBoys);
    }


    @PostMapping
    public ResponseEntity createParkingBoy(@RequestBody ParkingBoy parkingBoy) {
        final ParkingBoyResponse parkingBoyResponse = ParkingBoyResponse.create(parkingBoyRepository.save(parkingBoy));
        return ResponseEntity.created(URI.create("/parkingclerks")).body(parkingBoyResponse);
    }

    @GetMapping(path = "/{id}/orders")
    public ResponseEntity<ParkingOrderResponse[]> getAllAssociateParkingOrders(@PathVariable Long id){
        ParkingBoy parkingBoy= parkingBoyRepository.findOneById(id);
        return ResponseEntity.ok((ParkingOrderResponse[])parkingBoy.getParkingOrders().stream()
                .map(ParkingOrderResponse::create)
                .toArray(ParkingOrderResponse[]::new));
    }

    @PostMapping(path = "/{id}/orders")
    public ResponseEntity addParkingOrderToParkingBoy(@PathVariable Long id, @RequestBody ParkingBoyParkingOrderAssociationRequest parkingBoyParkingOrderAssociationRequest){
        Optional<ParkingBoy> parkingBoy=parkingBoyRepository.findById(id);
        if(!parkingBoy.isPresent()){
            return ResponseEntity.badRequest().build();
        }
        Optional<ParkingOrder> parkingOrder=parkingOrderRepository.findById(parkingBoyParkingOrderAssociationRequest.getParkingOrderId());
        if(!parkingOrder.isPresent()){
            return ResponseEntity.badRequest().build();
        }
        if(parkingOrder.get().getParkingBoy()!=null){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        parkingOrder.get().setParkingBoy(parkingBoy.get());
        parkingOrder.get().setStatus("accept");
        parkingOrderRepository.saveAndFlush(parkingOrder.get());
        return ResponseEntity.created(URI.create("/orders")).body(parkingOrder);
    }


    @PostMapping(path = "/{id}/parkinglots")
    public ResponseEntity addParkingLotToParkingBoy(@PathVariable Long id, @RequestBody ParkingBoyParkingLotAssociationRequest parkingBoyParkingLotAssociationRequest){
        Optional<ParkingBoy> parkingBoy=parkingBoyRepository.findById(id);

        if(!parkingBoy.isPresent()){
            return ResponseEntity.badRequest().build();
        }
        Optional<ParkingLot> parkingLot=parkingLotRepository.findById(parkingBoyParkingLotAssociationRequest.getParkingLotId());
        if(!parkingLot.isPresent()){
            return ResponseEntity.badRequest().build();
        }
        if(parkingLot.get().getParkingBoy()!=null){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        parkingLot.get().setParkingBoy(parkingBoy.get());
        parkingLotRepository.saveAndFlush(parkingLot.get());
        return ResponseEntity.created(URI.create("/parkingclerks/"+id+"/parkinglots")).body(parkingLot);

    }

    @GetMapping(path = "/{id}/parkinglots")
    public ResponseEntity getParkingLot(@PathVariable Long id){
        ParkingBoy parkingBoy = parkingBoyRepository.findOneById(id);
        return ResponseEntity.ok(parkingBoy.getParkingLots());
    }




}
