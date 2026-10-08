package com.cbgrievances.cb_grievances.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RoleCheckSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // Role the user picked on the login form (just a claim, not trusted)
        String selectedRole = request.getParameter("role");

        // Real role, decided by which table the account was found in
        boolean isWarden = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_WARDEN"));
        String actualRole = isWarden ? "WARDEN" : "RESIDENT";

        if (!actualRole.equals(selectedRole)) {
            // Wrong role: undo the login and show the same message as a wrong password,
            // so an attacker can't tell that the password was correct
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            response.sendRedirect("/login?error");
            return;
        }

        response.sendRedirect("/");
    }
}