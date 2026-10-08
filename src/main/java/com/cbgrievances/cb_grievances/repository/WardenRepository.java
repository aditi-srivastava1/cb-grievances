package com.cbgrievances.cb_grievances.repository;

import com.cbgrievances.cb_grievances.model.Warden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WardenRepository extends JpaRepository<Warden, Long> {
    Warden findByUsername(String username);
}