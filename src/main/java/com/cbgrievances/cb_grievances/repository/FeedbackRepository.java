package com.cbgrievances.cb_grievances.repository;

import com.cbgrievances.cb_grievances.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Feedback findByComplaintId(Long complaintId);
}