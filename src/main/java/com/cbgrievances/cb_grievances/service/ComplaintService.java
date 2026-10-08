package com.cbgrievances.cb_grievances.service;

import com.cbgrievances.cb_grievances.model.Complaint;
import com.cbgrievances.cb_grievances.model.Resident;
import com.cbgrievances.cb_grievances.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

    public Complaint fileComplaint(Complaint complaint) {
        complaint.setDateFiled(LocalDate.now());
        complaint.setStatus("Pending");
        return complaintRepository.save(complaint);
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    public List<Complaint> getComplaintsByResident(Long residentId) {
        return complaintRepository.findByResidentId(residentId);
    }

    public List<Complaint> getComplaintsByWing(String wing) {
        return complaintRepository.findByWing(wing);
    }

    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id).orElse(null);
    }

    public Complaint getComplaintByPhotoFileName(String fileName) {
        return complaintRepository.findByPhotoFileName(fileName);
    }

    public void updateStatus(Long complaintId, String newStatus) {
        Complaint complaint = complaintRepository.findById(complaintId).orElse(null);
        if (complaint == null) {
            return;
        }

        String oldStatus = complaint.getStatus();
        complaint.setStatus(newStatus);
        complaintRepository.save(complaint);

        // Notify only the first time it becomes Completed, not on every Update click
        boolean justResolved = "Completed".equals(newStatus) && !"Completed".equals(oldStatus);
        if (justResolved) {
            Resident resident = complaint.getResident();
            notificationService.notifyComplaintResolved(resident, complaint);
            emailService.sendComplaintResolved(
                    resident.getJnuId(),
                    resident.getName(),
                    complaint.getCategory(),
                    String.valueOf(complaint.getDateFiled()));
        }
    }

    public void reopenComplaint(Long complaintId) {
        Complaint complaint = complaintRepository.findById(complaintId).orElse(null);
        if (complaint != null) {
            complaint.setStatus("Reopened");
            complaintRepository.save(complaint);
        }
    }
}