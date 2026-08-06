package com.ems.employee.service;

import com.ems.employee.dto.EmployeeRequestDto;
import com.ems.employee.dto.EmployeeResponseDto;
import com.ems.employee.dto.PageResponse;

public interface EmployeeService {

    EmployeeResponseDto createEmployee(
            EmployeeRequestDto requestDto
    );

    PageResponse<EmployeeResponseDto> getEmployees(
            int page,
            int size,
            String sortBy,
            String direction
    );

    EmployeeResponseDto getEmployeeById(
            Long id
    );

    EmployeeResponseDto updateEmployee(
            Long id,
            EmployeeRequestDto requestDto
    );

    void deleteEmployee(Long id);

    PageResponse<EmployeeResponseDto> searchEmployees(
            String keyword,
            int page,
            int size,
            String sortBy,
            String direction
    );

    PageResponse<EmployeeResponseDto>
    getEmployeesByDepartment(
            String department,
            int page,
            int size
    );

    PageResponse<EmployeeResponseDto>
    getEmployeesByActiveStatus(
            Boolean active,
            int page,
            int size
    );

    PageResponse<EmployeeResponseDto> filterEmployees(
            String keyword,
            String department,
            Boolean active,
            int page,
            int size,
            String sortBy,
            String direction
    );
}