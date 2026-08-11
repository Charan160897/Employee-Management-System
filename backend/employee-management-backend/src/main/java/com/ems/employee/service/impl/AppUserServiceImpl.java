package com.ems.employee.service.impl;

import com.ems.employee.dto.LoginRequestDto;
import com.ems.employee.dto.LoginResponseDto;
import com.ems.employee.entity.AppUser;
import com.ems.employee.repository.AppUserRepository;
import com.ems.employee.service.AppUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.ems.employee.exception.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImpl
        implements AppUserService {

    private final AppUserRepository
            appUserRepository;

    private final PasswordEncoder
            passwordEncoder;

    public AppUserServiceImpl(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.appUserRepository =
                appUserRepository;

        this.passwordEncoder =
                passwordEncoder;
    }

    @Override
    public LoginResponseDto login(
            LoginRequestDto loginRequest
    ) {
        String username =
                loginRequest
                        .getUsername()
                        .trim();

        AppUser user =
                appUserRepository
                        .findByUsername(username)
                        .orElseThrow(
                                () ->
                                        new AuthenticationException(
                                                "Invalid username or password"
                                        )
                        );

        if (!user.isActive()) {
            throw new AuthenticationException(
                    "User account is inactive"
            );
        }

        boolean passwordMatches =
                passwordEncoder.matches(
                        loginRequest.getPassword(),
                        user.getPassword()
                );

        if (!passwordMatches) {
            throw new RuntimeException(
                    "Invalid username or password"
            );
        }

        return new LoginResponseDto(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                "Login successful"
        );
    }
}