package com.cbgrievances.cb_grievances.service;

import com.cbgrievances.cb_grievances.model.Resident;
import com.cbgrievances.cb_grievances.repository.ResidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ResidentService {

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Returns an error message, or null if the form data is fine
    public String validate(Resident resident) {
        String email = clean(resident.getJnuId()).toLowerCase();
        String name = clean(resident.getName());
        String password = resident.getPassword() == null ? "" : resident.getPassword();
        String room = clean(resident.getRoomNumber());
        String wing = resident.getWing();

        if (!email.matches("^[a-z0-9._%+-]+@jnu\\.ac\\.in$")) {
            return "Please use your JNU email ID (ending with @jnu.ac.in).";
        }
        if (residentRepository.findByJnuId(email) != null) {
            return "This JNU email ID is already registered. Please log in instead.";
        }
        if (name.isEmpty() || name.length() > 100) {
            return "Please enter your name (up to 100 characters).";
        }
        // BCrypt only uses the first 72 bytes, so longer passwords are not allowed
        if (password.length() < 8 || password.length() > 72) {
            return "Password must be between 8 and 72 characters.";
        }
        if (!"Boys".equals(wing) && !"Girls".equals(wing)) {
            return "Please select a valid wing.";
        }
        if (!room.matches("^[A-Za-z0-9-]{1,10}$")) {
            return "Room number can only contain letters, digits and hyphens (up to 10 characters).";
        }
        return null;
    }

    public Resident registerResident(Resident resident) {
        resident.setId(null);   // never let the form decide which row gets saved
        resident.setJnuId(clean(resident.getJnuId()).toLowerCase());
        resident.setName(clean(resident.getName()));
        resident.setRoomNumber(clean(resident.getRoomNumber()));
        resident.setPassword(passwordEncoder.encode(resident.getPassword()));
        return residentRepository.save(resident);
    }

    public Resident findByJnuId(String jnuId) {
        return residentRepository.findByJnuId(jnuId);
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }
}