package com.cbgrievances.cb_grievances.controller;

import com.cbgrievances.cb_grievances.model.Complaint;
import com.cbgrievances.cb_grievances.model.Feedback;
import com.cbgrievances.cb_grievances.model.Resident;
import com.cbgrievances.cb_grievances.service.ComplaintService;
import com.cbgrievances.cb_grievances.service.FeedbackService;
import com.cbgrievances.cb_grievances.service.FileStorageService;
import com.cbgrievances.cb_grievances.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private ResidentService residentService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FileStorageService fileStorageService;

    // Show the complaint form
    @GetMapping("/complaint")
    public String showComplaintForm(Model model) {
        model.addAttribute("complaint", new Complaint());
        return "complaint";
    }

    // Handle complaint submission (photo is optional)
    @PostMapping("/complaint")
    public String fileComplaint(@ModelAttribute Complaint complaint,
                                @RequestParam(value = "photo", required = false) MultipartFile photo,
                                Authentication authentication) {
        String jnuId = authentication.getName();
        Resident resident = residentService.findByJnuId(jnuId);

        String storedName;
        try {
            storedName = fileStorageService.store(photo);   // null if no photo chosen
        } catch (IllegalArgumentException | IOException e) {
            return "redirect:/complaint?error";
        }

        complaint.setId(null);   // never let the form decide which row gets saved
        complaint.setResident(resident);
        complaint.setWing(resident.getWing());
        complaint.setPhotoFileName(storedName);   // always set by the server, never by the form

        complaintService.fileComplaint(complaint);
        return "redirect:/";
    }

    // Show the logged-in resident's own complaints
    @GetMapping("/my-complaints")
    public String myComplaints(Model model, Authentication authentication) {
        String jnuId = authentication.getName();
        Resident resident = residentService.findByJnuId(jnuId);

        List<Complaint> complaints = complaintService.getComplaintsByResident(resident.getId());
        model.addAttribute("complaints", complaints);

        // IDs of complaints that have feedback, and of those with a low rating (2 or less)
        Set<Long> feedbackGiven = new HashSet<>();
        Set<Long> lowRated = new HashSet<>();
        for (Complaint c : complaints) {
            Feedback f = feedbackService.getFeedbackByComplaintId(c.getId());
            if (f != null) {
                feedbackGiven.add(c.getId());
                if (f.getStarRating() <= 2) {
                    lowRated.add(c.getId());
                }
            }
        }
        model.addAttribute("feedbackGiven", feedbackGiven);
        model.addAttribute("lowRated", lowRated);

        return "my-complaints";
    }

    // Reopen a completed complaint that got a low rating
    @PostMapping("/complaint/{id}/reopen")
    public String reopenComplaint(@PathVariable Long id, Authentication authentication) {
        Complaint complaint = complaintService.getComplaintById(id);
        if (complaint == null) {
            return "redirect:/my-complaints";
        }

        Feedback feedback = feedbackService.getFeedbackByComplaintId(id);

        boolean isOwner = complaint.getResident().getJnuId().equals(authentication.getName());
        boolean isCompleted = "Completed".equals(complaint.getStatus());
        boolean isLowRated = feedback != null && feedback.getStarRating() <= 2;

        if (isOwner && isCompleted && isLowRated) {
            feedbackService.deleteFeedback(feedback);
            complaintService.reopenComplaint(id);
        }
        return "redirect:/my-complaints";
    }

    // Serve a complaint photo, only to its owner or a warden
    @GetMapping("/photos/{fileName}")
    public ResponseEntity<Resource> viewPhoto(@PathVariable String fileName,
                                              Authentication authentication) {
        Complaint complaint = complaintService.getComplaintByPhotoFileName(fileName);
        if (complaint == null) {
            return ResponseEntity.notFound().build();
        }

        boolean isWarden = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_WARDEN"));
        boolean isOwner = complaint.getResident().getJnuId().equals(authentication.getName());
        if (!isWarden && !isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Path path = fileStorageService.load(fileName);
        if (path == null || !Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        MediaType type = fileName.toLowerCase().endsWith(".png")
                ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;
        return ResponseEntity.ok().contentType(type).body(new FileSystemResource(path));
    }
}