package com.cbgrievances.cb_grievances.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${app.mail.from:}")
    private String from;

    // When set (testing), every email goes here instead of the real resident
    @Value("${app.mail.override-to:}")
    private String overrideTo;

    // Shows once at startup whether the environment variables reached the app
    @PostConstruct
    void logSetup() {
        boolean configured = mailSender != null && !from.isBlank();
        log.info("Email setup: configured={}, testMode={}", configured, !overrideTo.isBlank());
    }

    // Runs in the background so the Warden's click is not slowed down
    @Async
    public void sendComplaintResolved(String toEmail, String residentName,
                                      String category, String dateFiled) {
        if (mailSender == null || from.isBlank()) {
            log.warn("Email is not configured, so no email was sent.");
            return;
        }
        try {
            boolean testMode = !overrideTo.isBlank();

            String body = "Hello " + residentName + ",\n\n"
                    + "Your complaint (" + category + ", filed on " + dateFiled + ") "
                    + "has been marked as resolved by the warden.\n\n"
                    + "Please log in to CB Grievances and give your feedback. "
                    + "If the problem is still not fixed, rate it 2 or lower and "
                    + "use the Reopen button on the My Complaints page.\n\n"
                    + "CB Grievances - Chandrabhaga Hostel";
            if (testMode) {
                body = "[TEST MODE - this email was meant for " + toEmail + "]\n\n" + body;
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(testMode ? overrideTo : toEmail);
            message.setSubject("Your complaint has been resolved - CB Grievances");
            message.setText(body);
            mailSender.send(message);
            log.info("Resolved-complaint email handed to the mail server (testMode={}).", testMode);
        } catch (Exception e) {
            // A failed email must never break the status update
            log.error("Could not send email: {}", e.getMessage());
        }
    }
}