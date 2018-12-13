package com.binluis.parkingsystem.controller;

import com.binluis.parkingsystem.domain.*;
import com.binluis.parkingsystem.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ParkingBoyResponse[]> getAllParkingBoys() {
        final ParkingBoyResponse[] parkingBoys = parkingBoyRepository.findAll().stream()
                .map(ParkingBoyResponse::create)
                .toArray(ParkingBoyResponse[]::new);
        return ResponseEntity.ok(parkingBoys);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ParkingBoyResponse> getParkingBoy(@PathVariable Long id) {
        Optional<ParkingBoy> parkingBoy = parkingBoyRepository.findById(id);
        if(!parkingBoy.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ParkingBoyResponse.create(parkingBoy.get()));
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity createParkingBoy(@RequestBody ParkingBoy parkingBoy) {
        final ParkingBoyResponse parkingBoyResponse = ParkingBoyResponse.create(parkingBoyRepository.save(parkingBoy));
        return ResponseEntity.created(URI.create("/parkingclerks")).body(parkingBoyResponse);
    }

    @GetMapping(path = "/{id}/orders")
    @PreAuthorize("hasRole('PARKINGCLERK')")
    public ResponseEntity<ParkingOrderResponse[]> getAllAssociateParkingOrders(@PathVariable Long id,@RequestParam(required = false) String[] status){
        ParkingBoy parkingBoy= parkingBoyRepository.findOneById(id);
        if(status!=null) {
            return ResponseEntity.ok((ParkingOrderResponse[]) parkingBoy.getParkingOrders().stream().filter(order -> {
                for (String s : status) {
                    if (order.getStatus().equals(s)) {
                        return true;
                    }
                }
                return false;
            })
                    .map(ParkingOrderResponse::create)
                    .toArray(ParkingOrderResponse[]::new));
        }else{
            return ResponseEntity.ok((ParkingOrderResponse[]) parkingBoy.getParkingOrders().stream().map(ParkingOrderResponse::create)
                    .toArray(ParkingOrderResponse[]::new));

        }
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
        parkingOrder.get().setStatus("accepted");
        parkingOrderRepository.saveAndFlush(parkingOrder.get());
        return ResponseEntity.created(URI.create("/orders")).body(parkingOrder);
    }


    @PostMapping(path = "/{id}/parkinglots")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('PARKINGCLERK')")
    public ResponseEntity getParkingLot(@PathVariable Long id){
        ParkingBoy parkingBoy = parkingBoyRepository.findOneById(id);
        List<ParkingLotResponse> parkingLotOfParkingBoy = parkingBoy.getParkingLots().stream().map(e -> {
            int availableCapacity = e.getCapacity() - e.getParkingOrders().size();
            return ParkingLotResponse.create(e.getId(), e.getName(), e.getCapacity(), availableCapacity);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(parkingLotOfParkingBoy);
    }

    @PutMapping(path = "/{id}/orders/{carNumber}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<ParkingOrder> fetchCar(@PathVariable Long id, @PathVariable String carNumber){
        Optional<ParkingBoy> parkingBoy = parkingBoyRepository.findById(id);
        Long orderId = null;
        if(!parkingBoy.isPresent()){
            return ResponseEntity.badRequest().build();
        }
        ParkingOrder selectedOrder = null;
        for (ParkingOrder parkingOrder: parkingBoy.get().getParkingOrders()){
            if(parkingOrder.getCarNumber().equals(carNumber)){
                orderId = parkingOrder.getId();
            }
        }
        if(orderId==null){
            return ResponseEntity.badRequest().build();
        }
        Optional<ParkingOrder> parkingOrder = parkingOrderRepository.findById(orderId);
        if(!parkingOrder.isPresent()){
            return ResponseEntity.badRequest().build();
        }
        parkingOrder.get().setStatus("Fetched");
        parkingOrderRepository.saveAndFlush(parkingOrder.get());
        return ResponseEntity.ok().body(parkingOrder.get());
    }
}
