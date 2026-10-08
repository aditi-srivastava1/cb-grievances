package com.cbgrievances.cb_grievances.config;

import com.cbgrievances.cb_grievances.model.Warden;
import com.cbgrievances.cb_grievances.repository.WardenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner createFirstWarden(WardenRepository wardenRepository,
                                               PasswordEncoder passwordEncoder,
                                               @Value("${app.warden.email:}") String emailValue,
                                               @Value("${app.warden.password:}") String password,
                                               @Value("${app.warden.name:Warden}") String name) {
        final String email = emailValue.trim();

        return args -> {
            if (email.isEmpty() || password.isEmpty()) {
                log.warn("WARDEN_EMAIL / WARDEN_PASSWORD are not set, so no warden account was created.");
                return;
            }
            if (password.length() < 8 || password.length() > 72) {
                log.warn("WARDEN_PASSWORD must be 8 to 72 characters, so no warden account was created.");
                return;
            }
            if (wardenRepository.findByUsername(email) != null) {
                return;   // already exists: leave the account and its password untouched
            }

            Warden warden = new Warden();
            warden.setUsername(email);
            warden.setPassword(passwordEncoder.encode(password));
            warden.setName(name);
            warden.setWing("All");
            wardenRepository.save(warden);
            log.info("First warden account created.");
        };
    }
}