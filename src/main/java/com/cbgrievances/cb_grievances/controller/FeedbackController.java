package com.cbgrievances.cb_grievances.controller;

import com.cbgrievances.cb_grievances.model.Complaint;
import com.cbgrievances.cb_grievances.model.Feedback;
import com.cbgrievances.cb_grievances.service.ComplaintService;
import com.cbgrievances.cb_grievances.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private ComplaintService complaintService;

    // Show the feedback form
    @GetMapping("/feedback/{complaintId}")
    public String showFeedbackForm(@PathVariable Long complaintId,
                                   Model model,
                                   Authentication authentication) {

        Complaint complaint = complaintService.getComplaintById(complaintId);

        if (!canGiveFeedback(complaint, authentication)) {
            return "redirect:/my-complaints";
        }

        model.addAttribute("feedback", new Feedback());
        model.addAttribute("complaint", complaint);
        return "feedback";
    }

    // Handle feedback submission
    @PostMapping("/feedback/{complaintId}")
    public String submitFeedback(@PathVariable Long complaintId,
                                 @ModelAttribute Feedback feedback,
                                 Authentication authentication) {

        Complaint complaint = complaintService.getComplaintById(complaintId);

        if (!canGiveFeedback(complaint, authentication)) {
            return "redirect:/my-complaints";
        }

        if (feedback.getStarRating() < 1 || feedback.getStarRating() > 5) {
            return "redirect:/feedback/" + complaintId;
        }

        feedbackService.saveFeedback(feedback, complaint);
        return "redirect:/my-complaints";
    }

    // The 3 security checks in one place
    private boolean canGiveFeedback(Complaint complaint, Authentication authentication) {
        if (complaint == null) {
            return false;
        }
        boolean isOwner = complaint.getResident().getJnuId().equals(authentication.getName());
        boolean isCompleted = "Completed".equals(complaint.getStatus());
        boolean alreadyGiven = feedbackService.feedbackExists(complaint.getId());

        return isOwner && isCompleted && !alreadyGiven;
    }
}