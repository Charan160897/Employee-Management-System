package com.ems.employee.controller;

import com.ems.employee.dto.EmployeeRequestDto;
import com.ems.employee.dto.EmployeeResponseDto;
import com.ems.employee.dto.PageResponse;
import com.ems.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(
            EmployeeService employeeService
    ) {
        this.employeeService = employeeService;
    }

    /*
     * Create a new employee.
     *
     * POST /api/employees
     */
    @PostMapping
    public ResponseEntity<EmployeeResponseDto>
    createEmployee(
            @Valid
            @RequestBody
            EmployeeRequestDto requestDto
    ) {
        EmployeeResponseDto createdEmployee =
                employeeService.createEmployee(
                        requestDto
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdEmployee);
    }

    /*
     * Get all employees using pagination and sorting.
     *
     * GET /api/employees
     */
    @GetMapping
    public ResponseEntity<
            PageResponse<EmployeeResponseDto>
            > getEmployees(
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String direction
    ) {
        PageResponse<EmployeeResponseDto> response =
                employeeService.getEmployees(
                        page,
                        size,
                        sortBy,
                        direction
                );

        return ResponseEntity.ok(response);
    }

    /*
     * Day 8 combined filtering endpoint.
     *
     * GET /api/employees/filter
     *
     * All filter parameters are optional.
     */
    @GetMapping("/filter")
    public ResponseEntity<
            PageResponse<EmployeeResponseDto>
            > filterEmployees(
            @RequestParam(required = false)
            String keyword,

            @RequestParam(required = false)
            String department,

            @RequestParam(required = false)
            Boolean active,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String direction
    ) {
        PageResponse<EmployeeResponseDto> response =
                employeeService.filterEmployees(
                        keyword,
                        department,
                        active,
                        page,
                        size,
                        sortBy,
                        direction
                );

        return ResponseEntity.ok(response);
    }

    /*
     * Search employees by first name, last name or email.
     *
     * GET /api/employees/search
     */
    @GetMapping("/search")
    public ResponseEntity<
            PageResponse<EmployeeResponseDto>
            > searchEmployees(
            @RequestParam
            String keyword,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size,

            @RequestParam(defaultValue = "firstName")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String direction
    ) {
        PageResponse<EmployeeResponseDto> response =
                employeeService.searchEmployees(
                        keyword,
                        page,
                        size,
                        sortBy,
                        direction
                );

        return ResponseEntity.ok(response);
    }

    /*
     * Filter employees by department.
     *
     * GET /api/employees/department
     */
    @GetMapping("/department")
    public ResponseEntity<
            PageResponse<EmployeeResponseDto>
            > getEmployeesByDepartment(
            @RequestParam
            String name,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size
    ) {
        PageResponse<EmployeeResponseDto> response =
                employeeService
                        .getEmployeesByDepartment(
                                name,
                                page,
                                size
                        );

        return ResponseEntity.ok(response);
    }

    /*
     * Filter employees by active status.
     *
     * GET /api/employees/status
     */
    @GetMapping("/status")
    public ResponseEntity<
            PageResponse<EmployeeResponseDto>
            > getEmployeesByStatus(
            @RequestParam
            Boolean active,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size
    ) {
        PageResponse<EmployeeResponseDto> response =
                employeeService
                        .getEmployeesByActiveStatus(
                                active,
                                page,
                                size
                        );

        return ResponseEntity.ok(response);
    }

    /*
     * Get one employee using ID.
     *
     * GET /api/employees/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto>
    getEmployeeById(
            @PathVariable
            Long id
    ) {
        EmployeeResponseDto employee =
                employeeService.getEmployeeById(id);

        return ResponseEntity.ok(employee);
    }

    /*
     * Update an employee.
     *
     * PUT /api/employees/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto>
    updateEmployee(
            @PathVariable
            Long id,

            @Valid
            @RequestBody
            EmployeeRequestDto requestDto
    ) {
        EmployeeResponseDto updatedEmployee =
                employeeService.updateEmployee(
                        id,
                        requestDto
                );

        return ResponseEntity.ok(
                updatedEmployee
        );
    }

    /*
     * Delete an employee.
     *
     * DELETE /api/employees/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable
            Long id
    ) {
        employeeService.deleteEmployee(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}