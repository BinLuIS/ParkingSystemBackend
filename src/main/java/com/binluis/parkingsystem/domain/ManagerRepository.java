package com.binluis.parkingsystem.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Manager findOneById(Long id);

    Manager findOneByName(String name);
}