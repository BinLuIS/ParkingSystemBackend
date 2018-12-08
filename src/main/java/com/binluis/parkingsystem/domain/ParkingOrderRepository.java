package com.binluis.parkingsystem.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingOrderRepository  extends JpaRepository<ParkingOrder, Long> {
}
