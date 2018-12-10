package com.binluis.parkingsystem.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingOrderRepository  extends JpaRepository<ParkingOrder, Long> {
    ParkingOrder findOneById (Long Id);

    List<ParkingOrder> findAllByStatus(String status);
}
