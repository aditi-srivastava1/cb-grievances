package com.cbgrievances.cb_grievances.controller;

import com.cbgrievances.cb_grievances.model.Resident;
import com.cbgrievances.cb_grievances.service.NotificationService;
import com.cbgrievances.cb_grievances.service.ResidentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class NotificationAdvice {

    @Autowired
    private ResidentService residentService;

    @Autowired
    private NotificationService notificationService;

    @ModelAttribute
    public void addNotifications(Authentication authentication, Model model,
                                 HttpServletRequest request) {
        // Only page loads need it (not form posts, and not every photo thumbnail)
        if (!"GET".equals(request.getMethod()) || request.getRequestURI().startsWith("/photos/")) {
            return;
        }
        if (authentication == null) {
            return;
        }
        boolean isResident = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_RESIDENT"));
        if (!isResident) {
            return;
        }
        Resident resident = residentService.findByJnuId(authentication.getName());
        if (resident == null) {
            return;
        }
        model.addAttribute("notifications", notificationService.getLatest(resident.getId()));
        model.addAttribute("unreadCount", notificationService.countUnread(resident.getId()));
    }
}