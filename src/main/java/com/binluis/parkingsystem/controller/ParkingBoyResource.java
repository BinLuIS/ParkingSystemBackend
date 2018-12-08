package com.binluis.parkingsystem.controller;

import com.binluis.parkingsystem.domain.*;
import com.binluis.parkingsystem.models.ParkingBoyParkingOrderAssociationRequest;
import com.binluis.parkingsystem.models.ParkingBoyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/parkingclerks")

public class ParkingBoyResource {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    ParkingBoyRepository parkingBoyRepository;

    @Autowired
    ParkingOrderRepository parkingOrderRepository;

    @GetMapping
    public ResponseEntity<ParkingBoyResponse[]> getAll() {
        final ParkingBoyResponse[] parkingBoys = parkingBoyRepository.findAll().stream()
                .map(ParkingBoyResponse::create)
                .toArray(ParkingBoyResponse[]::new);
        return ResponseEntity.ok(parkingBoys);
    }


    @PostMapping
    public ResponseEntity add(@RequestBody ParkingBoy parkingBoy) {
        final ParkingBoyResponse parkingBoyResponse = ParkingBoyResponse.create(parkingBoyRepository.save(parkingBoy));
        return ResponseEntity.created(URI.create("/parkingclerks")).body(parkingBoyResponse);
    }

    @PostMapping(path = "/{id}/parkingorders")
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
        parkingOrderRepository.save(parkingOrder.get());
        parkingOrderRepository.flush();
        return ResponseEntity.created(URI.create("/orders")).body(parkingOrder);
    }

}
