package com.cbgrievances.cb_grievances.controller;

import com.cbgrievances.cb_grievances.model.Resident;
import com.cbgrievances.cb_grievances.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ResidentController {

    @Autowired
    private ResidentService residentService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("resident", new Resident());
        return "register";
    }

    @PostMapping("/register")
    public String registerResident(@ModelAttribute Resident resident, Model model) {
        String error = residentService.validate(resident);

        if (error == null) {
            try {
                residentService.registerResident(resident);
                return "redirect:/login?registered";
            } catch (DataIntegrityViolationException e) {
                // two people registering the same email at the same moment
                error = "This JNU email ID is already registered. Please log in instead.";
            }
        }

        resident.setPassword(null);   // never send the password back to the page
        model.addAttribute("error", error);
        return "register";
    }
}