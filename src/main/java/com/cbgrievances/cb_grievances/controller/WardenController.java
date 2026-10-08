package com.cbgrievances.cb_grievances.controller;

import com.cbgrievances.cb_grievances.model.Complaint;
import com.cbgrievances.cb_grievances.model.Feedback;
import com.cbgrievances.cb_grievances.service.ComplaintService;
import com.cbgrievances.cb_grievances.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WardenController {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/warden/dashboard")
    public String dashboard(@RequestParam(required = false) String wing, Model model) {
        String selectedWing = normalizeWing(wing);

        List<Complaint> complaints = (selectedWing == null)
                ? complaintService.getAllComplaints()
                : complaintService.getComplaintsByWing(selectedWing);

        // newest first
        complaints = new ArrayList<>(complaints);
        complaints.sort(Comparator.comparing(Complaint::getId).reversed());

        model.addAttribute("complaints", complaints);
        model.addAttribute("selectedWing", selectedWing);

        // complaint id -> its feedback (only for complaints that have one)
        Map<Long, Feedback> feedbackMap = new HashMap<>();
        for (Complaint c : complaints) {
            Feedback f = feedbackService.getFeedbackByComplaintId(c.getId());
            if (f != null) {
                feedbackMap.put(c.getId(), f);
            }
        }
        model.addAttribute("feedbackMap", feedbackMap);

        return "warden-dashboard";
    }

    @PostMapping("/warden/complaint/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               @RequestParam(required = false) String wing) {
        complaintService.updateStatus(id, status);

        // stay on the same wing view after updating
        String selectedWing = normalizeWing(wing);
        return selectedWing == null
                ? "redirect:/warden/dashboard"
                : "redirect:/warden/dashboard?wing=" + selectedWing;
    }

    // Only "Boys" or "Girls" are accepted; anything else means "all wings"
    private String normalizeWing(String wing) {
        return ("Boys".equals(wing) || "Girls".equals(wing)) ? wing : null;
    }
}