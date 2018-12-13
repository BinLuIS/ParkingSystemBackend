package com.binluis.parkingsystem.controller;

import com.binluis.parkingsystem.Service.OrderService;
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
import java.util.ArrayList;
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

    @Autowired
    OrderService orderService;

    @GetMapping(produces = {"application/json"})
    public ResponseEntity<List<ParkingOrder>> getAllOrders(@RequestParam(required = false) String status,@RequestParam(required = false) String carNumber) {
        List<ParkingOrder> allOrders=null;
        if(status!=null){
           allOrders = orderService.getAllOrderByStatus(status);
        }else if(carNumber!=null){
            allOrders=orderService.getOrdersByCarNumber(carNumber);
        }else {
            allOrders = orderService.getAllOrders();
        }
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
        if(!parkingOrder.isPresent()){
            return ResponseEntity.notFound().build();
        }
        if(request.getStatus().equals("pendingFetching")) {
            boolean requestfetch=orderService.requestFetch(parkingOrder.get());
            if(!requestfetch) {
                ResponseEntity.badRequest().build();
            }
        }
        if(request.getStatus().equals("completed")){
            boolean fetched=orderService.fetchCar(parkingOrder.get());
            if(!fetched){
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.created(URI.create("/orders/"+id)).body(parkingOrder);
    }

    @PostMapping(produces = {"application/json"})
    public ResponseEntity createOrder(@RequestBody CreateParkingOrderRequest request){

        boolean validRequest=orderService.checkParkRequestValid(request);
        if(!validRequest){
            return ResponseEntity.badRequest().body("Invaild Car Number");
        }
        boolean noProcessingOrder=orderService.checkNoProcessingOrderOfCar(request.getCarNumber());
        if(!noProcessingOrder){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Invaild Car Number");
        }
        try{
            ParkingOrder order=orderService.createParkingOrder(request.getCarNumber());
            return ResponseEntity.created(URI.create("/orders/"+order.getId())).body(order);
        }
        catch (DataIntegrityViolationException e){
            return ResponseEntity.badRequest().build();
        }
    }

}