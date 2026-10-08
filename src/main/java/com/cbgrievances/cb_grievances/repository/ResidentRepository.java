package com.cbgrievances.cb_grievances.repository;

import com.cbgrievances.cb_grievances.model.Resident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResidentRepository extends JpaRepository<Resident, Long> {
    Resident findByJnuId(String jnuId);
}