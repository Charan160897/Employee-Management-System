package com.ems.employee.config;

import com.ems.employee.entity.AppUser;
import com.ems.employee.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createDefaultUsers(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            if (
                    !appUserRepository
                            .existsByUsername(
                                    "admin"
                            )
            ) {
                AppUser admin =
                        new AppUser(
                                "admin",
                                passwordEncoder.encode(
                                        "Admin@123"
                                ),
                                "ADMIN",
                                true
                        );

                appUserRepository.save(admin);
            }

            if (
                    !appUserRepository
                            .existsByUsername(
                                    "employee"
                            )
            ) {
                AppUser employee =
                        new AppUser(
                                "employee",
                                passwordEncoder.encode(
                                        "Employee@123"
                                ),
                                "USER",
                                true
                        );

                appUserRepository.save(employee);
            }
        };
    }
}