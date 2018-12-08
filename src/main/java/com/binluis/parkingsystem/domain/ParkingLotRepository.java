package com.binluis.parkingsystem.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingLotRepository  extends JpaRepository<ParkingOrder, Long> {
}
