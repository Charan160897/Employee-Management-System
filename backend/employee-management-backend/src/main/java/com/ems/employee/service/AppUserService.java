package com.ems.employee.service;

import com.ems.employee.dto.LoginRequestDto;
import com.ems.employee.dto.LoginResponseDto;

public interface AppUserService {

    LoginResponseDto login(
            LoginRequestDto loginRequest
    );
}