package com.cbgrievances.cb_grievances.controller;

import com.cbgrievances.cb_grievances.model.Resident;
import com.cbgrievances.cb_grievances.service.NotificationService;
import com.cbgrievances.cb_grievances.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NotificationController {

    @Autowired
    private ResidentService residentService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/notifications/read-all")
    public String markAllRead(Authentication authentication) {
        // A warden has no resident row, so this does nothing for them
        Resident resident = residentService.findByJnuId(authentication.getName());
        if (resident != null) {
            notificationService.markAllRead(resident.getId());
        }
        return "redirect:/";
    }
}