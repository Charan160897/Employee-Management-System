package com.ems.employee.controller;

import com.ems.employee.dto.LoginRequestDto;
import com.ems.employee.dto.LoginResponseDto;
import com.ems.employee.service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserService appUserService;

    public AuthController(
            AppUserService appUserService
    ) {
        this.appUserService =
                appUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto>
    login(
            @Valid
            @RequestBody
            LoginRequestDto loginRequest
    ) {
        return ResponseEntity.ok(
                appUserService.login(
                        loginRequest
                )
        );
    }
}