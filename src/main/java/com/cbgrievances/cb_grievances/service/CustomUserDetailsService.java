package com.cbgrievances.cb_grievances.service;

import com.cbgrievances.cb_grievances.model.Resident;
import com.cbgrievances.cb_grievances.model.Warden;
import com.cbgrievances.cb_grievances.repository.ResidentRepository;
import com.cbgrievances.cb_grievances.repository.WardenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private WardenRepository wardenRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. Check wardens table first
        Warden warden = wardenRepository.findByUsername(username);
        if (warden != null) {
            return User.builder()
                    .username(warden.getUsername())
                    .password(warden.getPassword())
                    .roles("WARDEN")
                    .build();
        }

        // 2. Otherwise check residents table
        Resident resident = residentRepository.findByJnuId(username);
        if (resident != null) {
            return User.builder()
                    .username(resident.getJnuId())
                    .password(resident.getPassword())
                    .roles("RESIDENT")
                    .build();
        }

        throw new UsernameNotFoundException("No user found with username: " + username);
    }
}