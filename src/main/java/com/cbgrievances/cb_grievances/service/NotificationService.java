package com.cbgrievances.cb_grievances.service;

import com.cbgrievances.cb_grievances.model.Complaint;
import com.cbgrievances.cb_grievances.model.Notification;
import com.cbgrievances.cb_grievances.model.Resident;
import com.cbgrievances.cb_grievances.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void notifyComplaintResolved(Resident resident, Complaint complaint) {
        Notification n = new Notification();
        n.setResident(resident);
        n.setComplaintId(complaint.getId());
        n.setMessage(truncate("Your complaint (" + complaint.getCategory()
                + ") has been marked as resolved. Please give your feedback.", 255));
        n.setCreatedAt(LocalDateTime.now());
        n.setSeen(false);
        notificationRepository.save(n);
    }

    public List<Notification> getLatest(Long residentId) {
        return notificationRepository.findTop5ByResidentIdOrderByCreatedAtDesc(residentId);
    }

    public long countUnread(Long residentId) {
        return notificationRepository.countByResidentIdAndSeenFalse(residentId);
    }

    public void markAllRead(Long residentId) {
        List<Notification> unread = notificationRepository.findByResidentIdAndSeenFalse(residentId);
        for (Notification n : unread) {
            n.setSeen(true);
        }
        notificationRepository.saveAll(unread);
    }

    // The category comes from a form, so keep the message within the column size
    private String truncate(String text, int max) {
        return text.length() <= max ? text : text.substring(0, max);
    }
}