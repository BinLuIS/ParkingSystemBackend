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
    public ResponseEntity<List<ParkingOrder>> getAllOrders(@RequestParam(required = false) String status) {
        List<ParkingOrder> allOrders=null;
        if(status!=null){
           allOrders = parkingOrderRepository.findAllByStatus(status);
        }else {
            allOrders = parkingOrderRepository.findAll();
        }
        return ResponseEntity.ok(allOrders);
    }

    @GetMapping(path = "?status={status}",produces = {"application/json"})
    public ResponseEntity<List<ParkingOrder>> getAllOrdersWithStaus(@PathVariable String status) {
        List<ParkingOrder> allOrders = parkingOrderRepository.findAllByStatus(status);
        return ResponseEntity.ok(allOrders);
    }
//
//    @PostMapping
//    public ResponseEntity add(@RequestBody ParkingOrder parkingOrder) {
//        parkingOrderRepository.save(parkingOrder);
//        parkingOrderRepository.flush();
//        return ResponseEntity.created(URI.create("/parkingclerks")).body(parkingOrder);
//    }


    @PatchMapping(path = "/{id}",produces = {"application/json"})
    public ResponseEntity makeCarFetchingRequest(@PathVariable Long id,@RequestBody CreateParkingOrderRequest request){
        Optional<ParkingOrder> parkingOrder=parkingOrderRepository.findById(id);
        System.out.println(parkingOrder.get().getCarNumber()+"!!!!!");
        if(!parkingOrder.isPresent()){
            return ResponseEntity.badRequest().build();
        }
        if(request.getStatus().equals("pendingFetching")) {
            System.out.println("in if 1");
            if (!parkingOrder.get().getStatus().equals("parked")) {
                return ResponseEntity.badRequest().build();
            }
            parkingOrder.get().setRequestType("fetching");
            parkingOrder.get().setStatus("pendingFetching");
            parkingOrderRepository.saveAndFlush(parkingOrder.get());
        }
        if(request.getStatus().equals("completed")){
            if(!parkingOrder.get().getStatus().equals("pendingFetching")) {
                return ResponseEntity.badRequest().build();
            }
            parkingOrder.get().setStatus("completed");
            parkingOrderRepository.saveAndFlush(parkingOrder.get());
        }
        return ResponseEntity.created(URI.create("/orders/"+id)).body(parkingOrder);
    }

}