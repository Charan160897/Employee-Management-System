package com.ems.employee.mapper;

import com.ems.employee.dto.EmployeeRequestDto;
import com.ems.employee.dto.EmployeeResponseDto;
import com.ems.employee.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public Employee toEntity(EmployeeRequestDto requestDto) {
        Employee employee = new Employee();

        employee.setFirstName(requestDto.getFirstName());
        employee.setLastName(requestDto.getLastName());
        employee.setEmail(normalizeEmail(requestDto.getEmail()));
        employee.setDepartment(requestDto.getDepartment());
        employee.setJobTitle(requestDto.getJobTitle());
        employee.setSalary(requestDto.getSalary());
        employee.setHireDate(requestDto.getHireDate());
        employee.setActive(requestDto.getActive());

        return employee;
    }

    public EmployeeResponseDto toResponseDto(Employee employee) {
        EmployeeResponseDto responseDto =
                new EmployeeResponseDto();

        responseDto.setId(employee.getId());
        responseDto.setFirstName(employee.getFirstName());
        responseDto.setLastName(employee.getLastName());
        responseDto.setFullName(
                employee.getFirstName()
                        + " "
                        + employee.getLastName()
        );
        responseDto.setEmail(employee.getEmail());
        responseDto.setDepartment(employee.getDepartment());
        responseDto.setJobTitle(employee.getJobTitle());
        responseDto.setSalary(employee.getSalary());
        responseDto.setHireDate(employee.getHireDate());
        responseDto.setActive(employee.getActive());

        return responseDto;
    }

    public void updateEntity(
            Employee employee,
            EmployeeRequestDto requestDto
    ) {
        employee.setFirstName(requestDto.getFirstName());
        employee.setLastName(requestDto.getLastName());
        employee.setEmail(normalizeEmail(requestDto.getEmail()));
        employee.setDepartment(requestDto.getDepartment());
        employee.setJobTitle(requestDto.getJobTitle());
        employee.setSalary(requestDto.getSalary());
        employee.setHireDate(requestDto.getHireDate());
        employee.setActive(requestDto.getActive());
    }

    private String normalizeEmail(String email) {
        return email == null
                ? null
                : email.trim().toLowerCase();
    }
}