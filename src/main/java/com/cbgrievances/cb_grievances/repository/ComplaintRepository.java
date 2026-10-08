package com.cbgrievances.cb_grievances.repository;

import com.cbgrievances.cb_grievances.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    Complaint findByPhotoFileName(String photoFileName);
    List<Complaint> findByWing(String wing);
    List<Complaint> findByResidentId(Long residentId);
}