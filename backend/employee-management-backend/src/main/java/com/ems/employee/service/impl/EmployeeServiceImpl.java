package com.ems.employee.service.impl;

import com.ems.employee.dto.EmployeeRequestDto;
import com.ems.employee.dto.EmployeeResponseDto;
import com.ems.employee.dto.PageResponse;
import com.ems.employee.entity.Employee;
import com.ems.employee.exception.DuplicateEmailException;
import com.ems.employee.exception.EmployeeNotFoundException;
import com.ems.employee.mapper.EmployeeMapper;
import com.ems.employee.repository.EmployeeRepository;
import com.ems.employee.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl
        implements EmployeeService {

    private static final int MAX_PAGE_SIZE = 100;

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(
            EmployeeRepository employeeRepository,
            EmployeeMapper employeeMapper
    ) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public EmployeeResponseDto createEmployee(
            EmployeeRequestDto requestDto
    ) {
        String normalizedEmail =
                normalizeEmail(requestDto.getEmail());

        if (employeeRepository
                .existsByEmailIgnoreCase(normalizedEmail)) {
            throw new DuplicateEmailException(
                    normalizedEmail
            );
        }

        requestDto.setEmail(normalizedEmail);

        Employee employee =
                employeeMapper.toEntity(requestDto);

        Employee savedEmployee =
                employeeRepository.save(employee);

        return employeeMapper.toResponseDto(savedEmployee);
    }

    @Override
    public PageResponse<EmployeeResponseDto>
    getEmployees(
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        Pageable pageable =
                createPageable(
                        page,
                        size,
                        sortBy,
                        direction
                );

        Page<Employee> employeePage =
                employeeRepository.findAll(pageable);

        return createPageResponse(employeePage);
    }

    @Override
    public EmployeeResponseDto getEmployeeById(Long id) {
        Employee employee = findEmployee(id);

        return employeeMapper.toResponseDto(employee);
    }

    @Override
    public EmployeeResponseDto updateEmployee(
            Long id,
            EmployeeRequestDto requestDto
    ) {
        Employee existingEmployee =
                findEmployee(id);

        String normalizedEmail =
                normalizeEmail(requestDto.getEmail());

        boolean emailUsedByAnotherEmployee =
                employeeRepository
                        .existsByEmailIgnoreCaseAndIdNot(
                                normalizedEmail,
                                id
                        );

        if (emailUsedByAnotherEmployee) {
            throw new DuplicateEmailException(
                    normalizedEmail
            );
        }

        requestDto.setEmail(normalizedEmail);

        employeeMapper.updateEntity(
                existingEmployee,
                requestDto
        );

        Employee updatedEmployee =
                employeeRepository.save(existingEmployee);

        return employeeMapper.toResponseDto(
                updatedEmployee
        );
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = findEmployee(id);
        employeeRepository.delete(employee);
    }

    @Override
    public PageResponse<EmployeeResponseDto>
    searchEmployees(
            String keyword,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        Pageable pageable =
                createPageable(
                        page,
                        size,
                        sortBy,
                        direction
                );

        String cleanedKeyword =
                keyword == null ? "" : keyword.trim();

        Page<Employee> employeePage =
                employeeRepository
                        .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                                cleanedKeyword,
                                cleanedKeyword,
                                cleanedKeyword,
                                pageable
                        );

        return createPageResponse(employeePage);
    }

    @Override
    public PageResponse<EmployeeResponseDto>
    getEmployeesByDepartment(
            String department,
            int page,
            int size
    ) {
        Pageable pageable =
                PageRequest.of(
                        validatePage(page),
                        validateSize(size),
                        Sort.by("firstName").ascending()
                );

        Page<Employee> employeePage =
                employeeRepository
                        .findByDepartmentContainingIgnoreCase(
                                department.trim(),
                                pageable
                        );

        return createPageResponse(employeePage);
    }

    @Override
    public PageResponse<EmployeeResponseDto>
    getEmployeesByActiveStatus(
            Boolean active,
            int page,
            int size
    ) {
        Pageable pageable =
                PageRequest.of(
                        validatePage(page),
                        validateSize(size),
                        Sort.by("firstName").ascending()
                );

        Page<Employee> employeePage =
                employeeRepository.findByActive(
                        active,
                        pageable
                );

        return createPageResponse(employeePage);
    }

    private Employee findEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(
                        () -> new EmployeeNotFoundException(id)
                );
    }

    private Pageable createPageable(
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        Sort.Direction sortDirection =
                "desc".equalsIgnoreCase(direction)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        String safeSortField =
                validateSortField(sortBy);

        return PageRequest.of(
                validatePage(page),
                validateSize(size),
                Sort.by(
                        sortDirection,
                        safeSortField
                )
        );
    }

    private int validatePage(int page) {
        return Math.max(page, 0);
    }

    private int validateSize(int size) {
        if (size < 1) {
            return 5;
        }

        return Math.min(size, MAX_PAGE_SIZE);
    }

    private String validateSortField(String sortBy) {
        if (sortBy == null) {
            return "id";
        }

        return switch (sortBy) {
            case "id",
                 "firstName",
                 "lastName",
                 "email",
                 "department",
                 "jobTitle",
                 "salary",
                 "hireDate",
                 "active" -> sortBy;
            default -> "id";
        };
    }

    private PageResponse<EmployeeResponseDto>
    createPageResponse(Page<Employee> employeePage) {

        List<EmployeeResponseDto> employeeDtos =
                employeePage.getContent()
                        .stream()
                        .map(employeeMapper::toResponseDto)
                        .toList();

        return new PageResponse<>(
                employeeDtos,
                employeePage.getNumber(),
                employeePage.getSize(),
                employeePage.getTotalElements(),
                employeePage.getTotalPages(),
                employeePage.isFirst(),
                employeePage.isLast()
        );
    }

    private String normalizeEmail(String email) {
        return email == null
                ? null
                : email.trim().toLowerCase();
    }
}