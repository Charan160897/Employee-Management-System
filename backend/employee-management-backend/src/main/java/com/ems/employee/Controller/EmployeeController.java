package com.ems.employee.Controller;

import com.ems.employee.dto.EmployeeRequestDto;
import com.ems.employee.dto.EmployeeResponseDto;
import com.ems.employee.dto.PageResponse;
import com.ems.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(
            EmployeeService employeeService
    ) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDto>
    createEmployee(
            @Valid
            @RequestBody
            EmployeeRequestDto requestDto
    ) {
        EmployeeResponseDto employee =
                employeeService.createEmployee(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(employee);
    }

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
        return ResponseEntity.ok(
                employeeService.getEmployees(
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto>
    getEmployeeById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                employeeService.getEmployeeById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDto>
    updateEmployee(
            @PathVariable Long id,

            @Valid
            @RequestBody
            EmployeeRequestDto requestDto
    ) {
        return ResponseEntity.ok(
                employeeService.updateEmployee(
                        id,
                        requestDto
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id
    ) {
        employeeService.deleteEmployee(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/search")
    public ResponseEntity<
            PageResponse<EmployeeResponseDto>
            > searchEmployees(
            @RequestParam String keyword,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size,

            @RequestParam(defaultValue = "firstName")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String direction
    ) {
        return ResponseEntity.ok(
                employeeService.searchEmployees(
                        keyword,
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    @GetMapping("/department")
    public ResponseEntity<
            PageResponse<EmployeeResponseDto>
            > getEmployeesByDepartment(
            @RequestParam String name,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size
    ) {
        return ResponseEntity.ok(
                employeeService
                        .getEmployeesByDepartment(
                                name,
                                page,
                                size
                        )
        );
    }

    @GetMapping("/status")
    public ResponseEntity<
            PageResponse<EmployeeResponseDto>
            > getEmployeesByStatus(
            @RequestParam Boolean active,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size
    ) {
        return ResponseEntity.ok(
                employeeService
                        .getEmployeesByActiveStatus(
                                active,
                                page,
                                size
                        )
        );
    }
}