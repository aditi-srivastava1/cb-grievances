package com.cbgrievances.cb_grievances.service;

import com.cbgrievances.cb_grievances.model.Complaint;
import com.cbgrievances.cb_grievances.model.Feedback;
import com.cbgrievances.cb_grievances.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public boolean feedbackExists(Long complaintId) {
        return feedbackRepository.findByComplaintId(complaintId) != null;
    }

    public Feedback saveFeedback(Feedback feedback, Complaint complaint) {
        feedback.setId(null);   // never let the form decide which row gets saved
        feedback.setComplaint(complaint);
        return feedbackRepository.save(feedback);
    }

    public Feedback getFeedbackByComplaintId(Long complaintId) {
        return feedbackRepository.findByComplaintId(complaintId);
    }

    public void deleteFeedback(Feedback feedback) {
        feedbackRepository.delete(feedback);
    }
}