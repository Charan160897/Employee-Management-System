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
import com.ems.employee.specification.EmployeeSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import com.ems.employee.dto.DepartmentCountDto;
import com.ems.employee.dto.EmployeeStatisticsDto;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final int DEFAULT_PAGE_SIZE = 5;
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

    /*
     * Create a new employee.
     */
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

        return employeeMapper.toResponseDto(
                savedEmployee
        );
    }

    /*
     * Return all employees with pagination and sorting.
     */
    @Override
    public PageResponse<EmployeeResponseDto> getEmployees(
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

    /*
     * Return one employee using the employee ID.
     */
    @Override
    public EmployeeResponseDto getEmployeeById(
            Long id
    ) {
        Employee employee = findEmployee(id);

        return employeeMapper.toResponseDto(employee);
    }

    /*
     * Update an existing employee.
     */
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
                employeeRepository.save(
                        existingEmployee
                );

        return employeeMapper.toResponseDto(
                updatedEmployee
        );
    }

    /*
     * Delete an employee.
     */
    @Override
    public void deleteEmployee(Long id) {
        Employee employee = findEmployee(id);

        employeeRepository.delete(employee);
    }

    /*
     * Search employees using first name, last name or email.
     */
    @Override
    public PageResponse<EmployeeResponseDto> searchEmployees(
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
                keyword == null
                        ? ""
                        : keyword.trim();

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

    /*
     * Return employees belonging to a department.
     */
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
                        Sort.by("firstName")
                                .ascending()
                );

        String cleanedDepartment =
                department == null
                        ? ""
                        : department.trim();

        Page<Employee> employeePage =
                employeeRepository
                        .findByDepartmentContainingIgnoreCase(
                                cleanedDepartment,
                                pageable
                        );

        return createPageResponse(employeePage);
    }

    /*
     * Return active or inactive employees.
     */
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
                        Sort.by("firstName")
                                .ascending()
                );

        Page<Employee> employeePage =
                employeeRepository.findByActive(
                        active,
                        pageable
                );

        return createPageResponse(employeePage);
    }

    /*
     * Day 8 combined filtering.
     *
     * This method can combine:
     * - keyword
     * - department
     * - active status
     * - pagination
     * - sorting
     */
    @Override
    public PageResponse<EmployeeResponseDto> filterEmployees(
            String keyword,
            String department,
            Boolean active,
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

        Specification<Employee> specification =
                Specification
                        .where(
                                EmployeeSpecification
                                        .hasKeyword(keyword)
                        )
                        .and(
                                EmployeeSpecification
                                        .hasDepartment(
                                                department
                                        )
                        )
                        .and(
                                EmployeeSpecification
                                        .hasActiveStatus(
                                                active
                                        )
                        );

        Page<Employee> employeePage =
                employeeRepository.findAll(
                        specification,
                        pageable
                );

        return createPageResponse(employeePage);
    }

    @Override
    public EmployeeStatisticsDto getEmployeeStatistics() {

        long totalEmployees =
                employeeRepository.count();

        long activeEmployees =
                employeeRepository.countByActive(true);

        long inactiveEmployees =
                employeeRepository.countByActive(false);

        BigDecimal averageSalary =
                employeeRepository.findAverageSalary();

        if (averageSalary == null) {
            averageSalary = BigDecimal.ZERO;
        }

        averageSalary = averageSalary.setScale(
                2,
                RoundingMode.HALF_UP
        );

        long totalDepartments =
                employeeRepository.countDistinctDepartments();

        List<Object[]> departmentRows =
                employeeRepository.countEmployeesByDepartment();

        List<DepartmentCountDto> departmentCounts =
                new ArrayList<>();

        for (Object[] row : departmentRows) {

            String department =
                    row[0] == null
                            ? "Unassigned"
                            : row[0].toString();

            long employeeCount =
                    ((Number) row[1]).longValue();

            departmentCounts.add(
                    new DepartmentCountDto(
                            department,
                            employeeCount
                    )
            );
        }

        return new EmployeeStatisticsDto(
                totalEmployees,
                activeEmployees,
                inactiveEmployees,
                averageSalary,
                totalDepartments,
                departmentCounts
        );
    }

    /*
     * Find an employee or throw a 404 exception.
     */
    private Employee findEmployee(Long id) {
        return employeeRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new EmployeeNotFoundException(
                                        id
                                )
                );
    }

    /*
     * Create pagination and sorting configuration.
     */
    private Pageable createPageable(
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        int safePage = validatePage(page);
        int safeSize = validateSize(size);

        String safeSortField =
                validateSortField(sortBy);

        Sort.Direction sortDirection =
                "desc".equalsIgnoreCase(direction)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Sort sort = Sort.by(
                sortDirection,
                safeSortField
        );

        return PageRequest.of(
                safePage,
                safeSize,
                sort
        );
    }

    /*
     * Page numbers cannot be negative.
     */
    private int validatePage(int page) {
        return Math.max(page, 0);
    }

    /*
     * Page size must be between 1 and 100.
     */
    private int validateSize(int size) {
        if (size < 1) {
            return DEFAULT_PAGE_SIZE;
        }

        return Math.min(
                size,
                MAX_PAGE_SIZE
        );
    }

    /*
     * Only valid Employee entity fields may be used for sorting.
     */
    private String validateSortField(
            String sortBy
    ) {
        if (sortBy == null || sortBy.isBlank()) {
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

    /*
     * Convert Page<Employee> to PageResponse<EmployeeResponseDto>.
     */
    private PageResponse<EmployeeResponseDto>
    createPageResponse(
            Page<Employee> employeePage
    ) {
        List<EmployeeResponseDto> employeeDtos =
                employeePage
                        .getContent()
                        .stream()
                        .map(
                                employeeMapper
                                        ::toResponseDto
                        )
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

    /*
     * Store email addresses consistently.
     */
    private String normalizeEmail(String email) {
        return email == null
                ? null
                : email.trim()
                .toLowerCase();
    }
}