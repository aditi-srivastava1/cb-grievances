package com.cbgrievances.cb_grievances.repository;

import com.cbgrievances.cb_grievances.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findTop5ByResidentIdOrderByCreatedAtDesc(Long residentId);

    long countByResidentIdAndSeenFalse(Long residentId);

    List<Notification> findByResidentIdAndSeenFalse(Long residentId);
}