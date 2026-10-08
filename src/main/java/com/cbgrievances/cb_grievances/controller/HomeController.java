package com.cbgrievances.cb_grievances.controller;

import com.cbgrievances.cb_grievances.model.Complaint;
import com.cbgrievances.cb_grievances.model.Resident;
import com.cbgrievances.cb_grievances.service.ComplaintService;
import com.cbgrievances.cb_grievances.service.FeedbackService;
import com.cbgrievances.cb_grievances.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ResidentService residentService;

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/")
    public String home(Authentication authentication, Model model) {

        // Warden goes straight to the warden dashboard
        boolean isWarden = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_WARDEN"));
        if (isWarden) {
            return "redirect:/warden/dashboard";
        }

        // Resident dashboard
        Resident resident = residentService.findByJnuId(authentication.getName());
        List<Complaint> complaints = complaintService.getComplaintsByResident(resident.getId());

        int pending = 0, inProcess = 0, completed = 0, awaitingFeedback = 0;
        for (Complaint c : complaints) {
            if ("Pending".equals(c.getStatus()) || "Reopened".equals(c.getStatus())) {
                pending++;
            } else if ("In Process".equals(c.getStatus())) {
                inProcess++;
            } else if ("Completed".equals(c.getStatus())) {
                completed++;
                if (!feedbackService.feedbackExists(c.getId())) {
                    awaitingFeedback++;
                }
            }
        }

        model.addAttribute("resident", resident);
        model.addAttribute("total", complaints.size());
        model.addAttribute("pending", pending);
        model.addAttribute("inProcess", inProcess);
        model.addAttribute("completed", completed);
        model.addAttribute("awaitingFeedback", awaitingFeedback);

        return "resident-dashboard";
    }
}