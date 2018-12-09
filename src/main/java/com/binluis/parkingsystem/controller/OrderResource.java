package com.binluis.parkingsystem.controller;

import com.binluis.parkingsystem.domain.*;
import com.binluis.parkingsystem.models.CreateParkingOrderRequest;
import com.binluis.parkingsystem.models.ParkingBoyParkingOrderAssociationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


@RestController
@CrossOrigin(origins = {"http://localhost:3000","https://parkingwebappmobile.herokuapp.com","https://binluis-parkingwebapp.herokuapp.com"})
@RequestMapping("/orders")
public class OrderResource {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    ParkingBoyRepository parkingBoyRepository;

    @Autowired
    ParkingOrderRepository parkingOrderRepository;

    @Autowired
    ParkingLotRepository parkingLotRepository;

    @GetMapping(produces = {"application/json"})
    public ResponseEntity<List<ParkingOrder>> getAllOrders() {
        List<ParkingOrder> allOrders = parkingOrderRepository.findAll();
        return ResponseEntity.ok(allOrders);
    }
//
//    @PostMapping
//    public ResponseEntity add(@RequestBody ParkingOrder parkingOrder) {
//        parkingOrderRepository.save(parkingOrder);
//        parkingOrderRepository.flush();
//        return ResponseEntity.created(URI.create("/parkingclerks")).body(parkingOrder);
//    }

    @PostMapping(produces = {"application/json"})
    public ResponseEntity createOrder(@RequestBody CreateParkingOrderRequest request){
        if(!request.isVaild()){

            return ResponseEntity.badRequest().body("Invaild Car Number");
        }
        try{
            ParkingOrder order = new ParkingOrder(request.getCarNumber(), "Parking", "Pending");
            parkingOrderRepository.saveAndFlush(order);
            return ResponseEntity.created(URI.create("/orders/"+order.getId())).body(order);
        }
        catch (DataIntegrityViolationException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = "/{id}",produces = {"application/json"})
    public ResponseEntity makeCarFetchingRequest(@PathVariable Long id){
        Optional<ParkingOrder> parkingOrder=parkingOrderRepository.findById(id);
        if(!parkingOrder.isPresent()){
            return ResponseEntity.badRequest().build();
        }
        if(!parkingOrder.get().getStatus().equals("Parked")){
            return ResponseEntity.badRequest().build();
        }
        parkingOrder.get().setRequestType("Fetching");
        parkingOrder.get().setStatus("Fetching");
        parkingOrderRepository.saveAndFlush(parkingOrder.get());
        return ResponseEntity.created(URI.create("/orders/"+id)).body(parkingOrder);
    }


}