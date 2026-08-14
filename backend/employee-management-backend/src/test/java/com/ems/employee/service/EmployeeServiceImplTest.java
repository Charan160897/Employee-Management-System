package com.ems.employee.service;

import com.ems.employee.dto.EmployeeRequestDto;
import com.ems.employee.dto.EmployeeResponseDto;
import com.ems.employee.entity.Employee;
import com.ems.employee.exception.DuplicateEmailException;
import com.ems.employee.exception.EmployeeNotFoundException;
import com.ems.employee.mapper.EmployeeMapper;
import com.ems.employee.repository.EmployeeRepository;
import com.ems.employee.service.impl.EmployeeServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeRequestDto requestDto;
    private EmployeeResponseDto responseDto;


    @BeforeEach
    void setUp() {

        employee = new Employee();

        employee.setId(1L);
        employee.setFirstName("Rahul");
        employee.setLastName("Kumar");
        employee.setEmail("rahul@example.com");
        employee.setDepartment("Engineering");
        employee.setJobTitle("Backend Developer");
        employee.setSalary(
                BigDecimal.valueOf(90000)
        );
        employee.setHireDate(
                LocalDate.of(2026, 7, 28)
        );
        employee.setActive(true);


        requestDto =
                new EmployeeRequestDto();

        requestDto.setFirstName("Rahul");
        requestDto.setLastName("Kumar");
        requestDto.setEmail(
                "rahul@example.com"
        );
        requestDto.setDepartment(
                "Engineering"
        );
        requestDto.setJobTitle(
                "Backend Developer"
        );
        requestDto.setSalary(
                BigDecimal.valueOf(90000)
        );
        requestDto.setHireDate(
                LocalDate.of(2026, 7, 28)
        );
        requestDto.setActive(true);


        responseDto =
                new EmployeeResponseDto();

        responseDto.setId(1L);
        responseDto.setFirstName("Rahul");
        responseDto.setLastName("Kumar");
        responseDto.setFullName(
                "Rahul Kumar"
        );
        responseDto.setEmail(
                "rahul@example.com"
        );
        responseDto.setDepartment(
                "Engineering"
        );
        responseDto.setJobTitle(
                "Backend Developer"
        );
        responseDto.setSalary(
                BigDecimal.valueOf(90000)
        );
        responseDto.setHireDate(
                LocalDate.of(2026, 7, 28)
        );
        responseDto.setActive(true);
    }


    // ==========================================
    // CREATE EMPLOYEE SUCCESS TEST
    // ==========================================

    @Test
    void createEmployee_shouldReturnCreatedEmployee() {

        when(
                employeeRepository
                        .existsByEmailIgnoreCase(
                                "rahul@example.com"
                        )
        ).thenReturn(false);

        when(
                employeeMapper.toEntity(
                        requestDto
                )
        ).thenReturn(employee);

        when(
                employeeRepository.save(
                        employee
                )
        ).thenReturn(employee);

        when(
                employeeMapper.toResponseDto(
                        employee
                )
        ).thenReturn(responseDto);


        EmployeeResponseDto result =
                employeeService
                        .createEmployee(
                                requestDto
                        );


        assertThat(result)
                .isNotNull();

        assertThat(
                result.getId()
        ).isEqualTo(1L);

        assertThat(
                result.getEmail()
        ).isEqualTo(
                "rahul@example.com"
        );


        verify(
                employeeRepository
        ).save(employee);


        verify(
                auditLogService
        ).log(
                anyString(),
                eq("CREATE"),
                eq("Rahul Kumar")
        );
    }


    // ==========================================
    // CREATE DUPLICATE EMAIL TEST
    // ==========================================

    @Test
    void createEmployee_shouldThrowDuplicateEmailException() {

        when(
                employeeRepository
                        .existsByEmailIgnoreCase(
                                "rahul@example.com"
                        )
        ).thenReturn(true);


        assertThatThrownBy(
                () ->
                        employeeService
                                .createEmployee(
                                        requestDto
                                )
        )
                .isInstanceOf(
                        DuplicateEmailException.class
                );


        verify(
                employeeRepository,
                never()
        ).save(
                any()
        );


        verify(
                auditLogService,
                never()
        ).log(
                anyString(),
                anyString(),
                anyString()
        );
    }


    // ==========================================
    // GET EMPLOYEE BY ID SUCCESS TEST
    // ==========================================

    @Test
    void getEmployeeById_shouldReturnEmployee() {

        when(
                employeeRepository
                        .findById(1L)
        ).thenReturn(
                Optional.of(employee)
        );

        when(
                employeeMapper
                        .toResponseDto(
                                employee
                        )
        ).thenReturn(
                responseDto
        );


        EmployeeResponseDto result =
                employeeService
                        .getEmployeeById(
                                1L
                        );


        assertThat(result)
                .isNotNull();

        assertThat(
                result.getId()
        ).isEqualTo(1L);

        assertThat(
                result.getFullName()
        ).isEqualTo(
                "Rahul Kumar"
        );

        assertThat(
                result.getDepartment()
        ).isEqualTo(
                "Engineering"
        );


        verify(
                employeeRepository
        ).findById(
                1L
        );

        verify(
                employeeMapper
        ).toResponseDto(
                employee
        );
    }


    // ==========================================
    // GET EMPLOYEE NOT FOUND TEST
    // ==========================================

    @Test
    void getEmployeeById_shouldThrowWhenEmployeeMissing() {

        when(
                employeeRepository
                        .findById(999L)
        ).thenReturn(
                Optional.empty()
        );


        assertThatThrownBy(
                () ->
                        employeeService
                                .getEmployeeById(
                                        999L
                                )
        )
                .isInstanceOf(
                        EmployeeNotFoundException.class
                );
    }


    // ==========================================
    // UPDATE EMPLOYEE SUCCESS TEST
    // ==========================================

    @Test
    void updateEmployee_shouldUpdateExistingEmployee() {

        when(
                employeeRepository
                        .findById(1L)
        ).thenReturn(
                Optional.of(employee)
        );


        when(
                employeeRepository
                        .existsByEmailIgnoreCaseAndIdNot(
                                "rahul@example.com",
                                1L
                        )
        ).thenReturn(false);


        doNothing()
                .when(
                        employeeMapper
                )
                .updateEntity(
                        employee,
                        requestDto
                );


        when(
                employeeRepository
                        .save(employee)
        ).thenReturn(
                employee
        );


        when(
                employeeMapper
                        .toResponseDto(
                                employee
                        )
        ).thenReturn(
                responseDto
        );


        EmployeeResponseDto result =
                employeeService
                        .updateEmployee(
                                1L,
                                requestDto
                        );


        assertThat(result)
                .isNotNull();

        assertThat(
                result.getId()
        ).isEqualTo(
                1L
        );


        verify(
                employeeMapper
        ).updateEntity(
                employee,
                requestDto
        );


        verify(
                employeeRepository
        ).save(
                employee
        );


        verify(
                auditLogService
        ).log(
                anyString(),
                eq("UPDATE"),
                eq("Rahul Kumar")
        );
    }


    // ==========================================
    // DELETE EMPLOYEE SUCCESS TEST
    // ==========================================

    @Test
    void deleteEmployee_shouldDeleteExistingEmployee() {

        when(
                employeeRepository
                        .findById(1L)
        ).thenReturn(
                Optional.of(employee)
        );


        employeeService
                .deleteEmployee(
                        1L
                );


        verify(
                employeeRepository
        ).delete(
                employee
        );


        verify(
                auditLogService
        ).log(
                anyString(),
                eq("DELETE"),
                eq("Rahul Kumar")
        );
    }
}