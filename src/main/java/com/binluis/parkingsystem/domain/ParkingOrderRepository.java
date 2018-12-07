package com.binluis.parkingsystem.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingOrderRepository  extends JpaRepository<ParkingOrder, Long> {
}
