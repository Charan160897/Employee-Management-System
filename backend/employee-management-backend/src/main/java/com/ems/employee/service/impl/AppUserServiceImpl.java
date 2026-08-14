package com.ems.employee.service.impl;

import com.ems.employee.dto.LoginRequestDto;
import com.ems.employee.dto.LoginResponseDto;
import com.ems.employee.entity.AppUser;
import com.ems.employee.repository.AppUserRepository;
import com.ems.employee.service.AppUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.ems.employee.exception.AuthenticationException;
import com.ems.employee.security.JwtService;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImpl
        implements AppUserService {

    private final AppUserRepository
            appUserRepository;

    private final PasswordEncoder
            passwordEncoder;
    private final JwtService jwtService;

    public AppUserServiceImpl(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.appUserRepository =
                appUserRepository;

        this.passwordEncoder =
                passwordEncoder;

        this.jwtService =
                jwtService;
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
                        .findByUsername(
                                username
                        )
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
            throw new AuthenticationException(
                    "Invalid username or password"
            );
        }

        String token =
                jwtService.generateToken(
                        user.getId(),
                        user.getUsername(),
                        user.getRole()
                );

        return new LoginResponseDto(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                token,
                "Login successful"
        );
    }}