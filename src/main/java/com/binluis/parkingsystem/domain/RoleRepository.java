package com.binluis.parkingsystem.domain;

import com.binluis.parkingsystem.models.Role;
import com.binluis.parkingsystem.models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}