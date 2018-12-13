package com.binluis.parkingsystem.Service;

import com.binluis.parkingsystem.domain.ParkingOrder;
import com.binluis.parkingsystem.domain.ParkingOrderRepository;
import com.binluis.parkingsystem.models.CreateParkingOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    ParkingOrderRepository parkingOrderRepository;

    public boolean requestFetch(ParkingOrder parkingOrder){
        if (!parkingOrder.getStatus().equals("parked")) {
            return false;
        }
        parkingOrder.setRequestType("fetching");
        parkingOrder.setStatus("pendingFetching");
        parkingOrderRepository.saveAndFlush(parkingOrder);
        return  true;
    }

    public List<ParkingOrder> getAllOrderByStatus(String status){
        return parkingOrderRepository.findAllByStatus(status);
    }

    public List<ParkingOrder> getOrdersByCarNumber(String carNumber) {
        return parkingOrderRepository.findAllByCarNumber(carNumber);
    }

    public List<ParkingOrder> getAllOrders() {
        return parkingOrderRepository.findAll();
    }

    public boolean fetchCar(ParkingOrder parkingOrder) {
        if(!parkingOrder.getStatus().equals("pendingFetching")) {
            return false;
        }
        parkingOrder.setStatus("completed");
        parkingOrderRepository.saveAndFlush(parkingOrder);
        return true;
    }

    public boolean checkParkRequestValid(CreateParkingOrderRequest request) {
        return request.isVaild();
    }

    public boolean checkNoProcessingOrderOfCar(String carNumber){
        List<ParkingOrder> existingOrders=getOrdersByCarNumber(carNumber);
        if(existingOrders.stream().filter(order->!order.getStatus().equals("completed")).toArray().length>0){
            return false;
        }
        return true;
    }

    public ParkingOrder createParkingOrder(String carNumber) {
        ParkingOrder order = new ParkingOrder(carNumber,"parking", "pendingParking");
        return parkingOrderRepository.saveAndFlush(order);
    }
}
